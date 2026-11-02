import os
import json

import httpx
from openai import AsyncOpenAI
from app.prompts import INTERVIEWER_SYSTEM_PROMPT, REPORT_SYSTEM_PROMPT
from app.schemas import AgentChatReq, AgentChatResp, AgentReportReq, AgentReportResp

# 初始化 DeepSeek 异步客户端
# 默认直连(本机测试直连可达);如需走代理,在 .env 里配置 DEEPSEEK_PROXY=http://127.0.0.1:7890
client = AsyncOpenAI(
    api_key=os.getenv("DEEPSEEK_API_KEY"),
    base_url="https://api.deepseek.com",
    http_client=httpx.AsyncClient(proxy=os.getenv("DEEPSEEK_PROXY") or None)
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


async def generate_interview_report(req: AgentReportReq) -> AgentReportResp:
    """整场面试结束后,把全部对话记录喂给 DeepSeek 做复盘,生成结构化评估报告"""
    # 1. 组装报告专用 System Prompt
    level_name = LEVEL_MAP.get(req.targetLevel, "未知")
    system_content = REPORT_SYSTEM_PROMPT.format(
        position_name=req.positionName,
        level_name=level_name
    )

    messages = [{"role": "system", "content": system_content}]

    # 2. 灌入整场面试记录 (Java 端按轮次排序后传过来的)
    for msg in req.dialogues:
        role = "assistant" if msg.role == "ai" else "user"
        messages.append({"role": role, "content": msg.content})

    # 3. 结尾再强调一次任务,确保模型输出报告而不是继续对话
    messages.append({"role": "user", "content": "请基于以上整场面试记录，生成最终评估报告JSON。"})

    # 4. 调用 DeepSeek (报告是离线任务,temperature 用低一点,结果更稳定)
    response = await client.chat.completions.create(
        model="deepseek-chat",
        messages=messages,
        response_format={"type": "json_object"},
        temperature=0.4
    )

    # 5. 解析结果
    raw_content = response.choices[0].message.content
    result_dict = json.loads(raw_content)

    # 6. radarDimensions 是嵌套 dict,序列化成 JSON 字符串方便 Java 端落库
    radar = result_dict.get("radarDimensions", {})
    radar_str = json.dumps(radar, ensure_ascii=False) if isinstance(radar, dict) else str(radar)

    return AgentReportResp(
        totalScore=float(result_dict.get("totalScore", 0.0)),
        radarDimensions=radar_str,
        highlights=result_dict.get("highlights", ""),
        weaknesses=result_dict.get("weaknesses", ""),
        learningAdvice=result_dict.get("learningAdvice", "")
    )
