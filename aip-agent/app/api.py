from fastapi import APIRouter
from app.schemas import AgentChatReq, AgentChatResp
from app.services.llm_service import generate_interview_reply
from app.services.asr_service import transcribe_audio
from app.services.tts_service import synthesize_text  # 引入我们刚写的 TTS 服务

router = APIRouter()

@router.post("/chat", response_model=AgentChatResp)
async def agent_chat(req: AgentChatReq):

    # 语音识别 (ASR)
    if req.audioUrl:
        recognized_text = await transcribe_audio(req.audioUrl)
        req.currentInput = recognized_text if recognized_text else "（候选人提交了一段语音，但无法听清）"

    if not req.currentInput:
        req.currentInput = "你好"

    # 大模型思考 (LLM)
    result: AgentChatResp = await generate_interview_reply(req)

    # 文字转语音 (TTS)
    if result.aiReply:
        # 给模型生成的文本配音
        audio_b64 = await synthesize_text(result.aiReply)
        result.ttsAudioBase64 = audio_b64

    return result
