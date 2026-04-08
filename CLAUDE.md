# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

AI面试平台 (AI Interview Platform) — an AI-powered online interview system. Three independently deployed services live in one monorepo:

| Directory | Stack | Purpose |
|---|---|---|
| `aip-frontend` | Vue 2.6 + Element UI (若依 RuoYi-Vue template) | Admin management UI |
| `aip-backend` | Spring Boot 2.5.15 + MyBatis + MySQL + Redis (若依 RuoYi 3.9.2) | Backend API server (port 9080) |
| `aip-agent` | Python FastAPI + DeepSeek LLM + Aliyun ASR/TTS | AI interview agent (port 8000) |

## Common Commands

### Frontend (`aip-frontend/`)
```bash
npm install                  # install dependencies
npm run dev                  # dev server on port 80, proxies /dev-api → localhost:9080
npm run build:prod           # production build → dist/
npm run build:stage          # staging build
```
No lint or test scripts are configured.

### Backend (`aip-backend/`)
```bash
mvn clean package                              # build all modules
mvn clean package -pl ruoyi-admin -am          # build only the runnable admin module with deps
java -jar ruoyi-admin/target/ruoyi-admin.jar   # run after build
```
Entry point: `ruoyi-admin` module → `com.ruoyi.RuoYiApplication`. Java 1.8 required. No test suites exist.

### Agent (`aip-agent/`)
```bash
pip install -r requirements.txt
python main.py               # FastAPI on port 8000, auto-reload enabled
```
Requires `.env` with `DEEPSEEK_API_KEY` and `ALIYUN_API_KEY`.

## Architecture

### Backend Module Structure (Maven multi-module)
- **ruoyi-admin** — Spring Boot entry point, controllers, config files (`application.yml`, `application-druid.yml`)
- **ruoyi-framework** — Security (JWT filter, Spring Security config), interceptors, AOP
- **ruoyi-system** — **All AIP business code** lives here, plus RuoYi system services
- **ruoyi-common** — Shared utils, base classes, annotations
- **ruoyi-quartz** — Scheduled tasks
- **ruoyi-generator** — Code generator

### AIP Business Code
All interview-domain code lives under `ruoyi-system/src/main/java/com/ruoyi/aip/`:
- `controller/manage/` — CRUD for positions, questions, sessions, dialogues, reports, knowledge docs
- `controller/control/AipInterviewController.java` — Interview flow control (start, chat)
- `domain/` — Entity classes (MyBatis) and `dto/` for request/response DTOs
- `mapper/` — MyBatis mapper interfaces; XML in `ruoyi-system/src/main/resources/mapper/aip/`
- `service/` — Service interfaces + `impl/` implementations
- `config/MinioConfig.java` — MinIO bean configuration
- `utils/MinioUtil.java` — MinIO file upload operations

### Interview Flow (the core business logic)

The main interview pipeline lives in `AipInterviewSessionServiceImpl`:

1. **Start** (`POST /api/control/start`) — Creates or resumes an `AipInterviewSession` (status=0 means in-progress). Returns session ID.
2. **Chat** (`POST /api/control/chat`) — The critical path:
   - Validates session is active (status=0)
   - Appends user message to Redis history (key: `aip:interview:ctx:{sessionId}`, TTL 2h)
   - Builds `AgentChatReqDTO` with position info, target level, and last 8 history messages
   - Calls agent at `http://127.0.0.1:8000/api/agent/chat` via RestTemplate
   - Appends AI response to Redis history
   - Async inserts dialogue record to DB (currently uses raw `new Thread()`)
   - Returns `AgentChatRespDTO` (aiReply, isFinished, turnScore)
3. **End** — Updates session status to 1 (completed) or 2 (abnormal)

### Agent Pipeline (`POST /api/agent/chat`)

Defined in `aip-agent/app/api.py`, orchestrates three services:

1. **ASR** (`asr_service.py`) — If `audioUrl` provided, downloads from MinIO via httpx, transcribes with Aliyun DashScope `sensevoice-v1`
2. **LLM** (`llm_service.py`) — Calls DeepSeek (`deepseek-chat`) with structured system prompt from `prompts.py`. Forces JSON output with `response_format={"type": "json_object"}`. Returns `aiReply`, `isFinished`, `turnScore`
3. **TTS** (`tts_service.py`) — Converts AI reply to speech via Aliyun DashScope `sambert-zhichu-v1`, returns Base64-encoded WAV

Schemas in `app/schemas.py`. Prompt template in `app/prompts.py` — parameterized by `{position_name}` and `{level_name}` (maps 1-4 → 实习生/初级/中级/高级).

### Frontend Structure

RuoYi-Vue admin template (Vue 2 + Element UI + Vuex + Vue Router in history mode).

- **API layer**: `src/api/aip/` — mirrors backend CRUD endpoints. All use `src/utils/request.js` (Axios with JWT token injection, duplicate-submit prevention, error code mapping)
- **Business views**: `src/views/aip/` — position, question, session, dialogue, report, doc, InterviewChat
- **InterviewChat** (`src/views/aip/InterviewChat/index.vue`) — Chat UI with session sidebar, message bubbles, typewriter effect for AI replies, thinking indicator. Currently uses mock API calls (TODO: real integration)
- **Auth**: JWT token stored via js-cookie, route guards in `src/permission.js`, permission directive `v-hasPermi`
- **Dynamic routing**: Routes loaded from backend based on user roles/permissions

Environment files: `.env.development` sets `VUE_APP_BASE_API=/dev-api`, `.env.production` uses `/prod-api`.

## Key Integration Points
- Frontend proxies `VUE_APP_BASE_API` to backend at `localhost:9080` (configured in `vue.config.js`)
- Backend calls agent at `localhost:8000/api/agent/chat` (hardcoded in `AipInterviewSessionServiceImpl`, TODO: move to config)
- File storage uses MinIO (config in `application.yml` under `minio:`)
- Backend uses Redis for JWT token management and interview history caching (`aip:interview:ctx:{sessionId}`)
- Auth uses JWT tokens (header: `Authorization`, 30-min expiry)
- Database password from env var `AIP_DB_PWD`

## Conventions
- Backend follows RuoYi naming: `listXxx`, `getXxx`, `addXxx`, `editXxx`, `removeXxx` for controller methods; `selectXxxList`, `insertXxx`, `updateXxx`, `deleteXxxByIds` for mappers
- Frontend API modules follow pattern: `listXxx(query)`, `getXxx(id)`, `addXxx(data)`, `updateXxx(data)`, `delXxx(id)`
- MyBatis XML mappers live alongside Java code in `ruoyi-system/src/main/resources/mapper/aip/`
- AIP database tables prefixed with `aip_` (e.g., `aip_interview_session`, `aip_position`, `aip_question`)
- Agent services wrap blocking SDK calls (DashScope) in async executors
