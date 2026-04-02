import os
import json

import httpx
from openai import AsyncOpenAI
from app.prompts import INTERVIEWER_SYSTEM_PROMPT
from app.schemas import AgentChatReq, AgentChatResp

# 初始化 DeepSeek 异步客户端
client = AsyncOpenAI(
    api_key=os.getenv("DEEPSEEK_API_KEY"),
    base_url="https://api.deepseek.com",
    # 如果开了代理软件（如 Clash），把下面的注释打开，确认代理端口是 7890
    http_client=httpx.AsyncClient(proxy="http://127.0.0.1:7890")
)

LEVEL_MAP = {1: "实习生", 2: "初级", 3: "中级", 4: "高级"}

async def generate_interview_reply(req: AgentChatReq) -> AgentChatResp:
    # 1. 组装 System Prompt
    level_name = LEVEL_MAP.get(req.targetLevel, "未知")
    system_content = INTERVIEWER_SYSTEM_PROMPT.format(
        position_name=req.positionName,
        level_name=level_name
    )

    messages = [{"role": "system", "content": system_content}]

    # 2. 灌入历史记忆 (Redis 传过来的)
    for msg in req.history:
        # FastAPI 里的 role 规范：ai -> assistant, user -> user
        role = "assistant" if msg.role == "ai" else "user"
        messages.append({"role": role, "content": msg.content})

    # 3. 灌入当次最新回答
    messages.append({"role": "user", "content": req.currentInput})

    try:
        # 4. 召唤 DeepSeek 发起非阻塞请求
        response = await client.chat.completions.create(
            model="deepseek-chat",
            messages=messages,
            response_format={"type": "json_object"}, # 强制要求 DeepSeek 返回 JSON
            temperature=0.7 # 控制面试官的严苛程度，0.7 比较适中
        )

        # 5. 解析并清洗 JSON 结果
        raw_content = response.choices[0].message.content
        result_dict = json.loads(raw_content)

        return AgentChatResp(
            aiReply=result_dict.get("aiReply", "抱歉，我的网络信号不太好，请您继续。"),
            isFinished=result_dict.get("isFinished", False),
            turnScore=float(result_dict.get("turnScore", 0.0))
        )

    except Exception as e:
        print(f"[LLM Service Error]: {e}")
        # 兜底返回，防止 Java 端崩溃
        return AgentChatResp(
            aiReply="系统大脑正在升级，请重试一遍你的回答。",
            isFinished=False,
            turnScore=0.0
        )
