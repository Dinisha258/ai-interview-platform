from pydantic import BaseModel
from typing import List, Optional

class ChatMessage(BaseModel):
    role: str
    content: str

class AgentChatReq(BaseModel):
    sessionId: int
    currentInput: Optional[str] = None # 可选，语音模式下这个字段可能是空的
    audioUrl: Optional[str] = None     # 接收 MinIO 的录音下载链接
    positionName: str
    targetLevel: int
    history: List[ChatMessage] = []

class AgentChatResp(BaseModel):
    aiReply: str
    isFinished: bool
    turnScore: float
    ttsAudioBase64: str = ""  # 直接返给前端播放的音频流
