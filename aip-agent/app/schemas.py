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

class AgentReportReq(BaseModel):
    """报告生成请求:整场面试结束后,Java端把全部对话记录打包发过来"""
    sessionId: int
    positionName: str
    targetLevel: int
    dialogues: List[ChatMessage] = []  # 整场对话 [{role, content}, ...]

class AgentReportResp(BaseModel):
    """报告生成结果:Java端落库到 aip_interview_report 表"""
    totalScore: float = 0.0
    radarDimensions: str = ""      # JSON字符串: {"专业知识":85, "逻辑表达":80, ...}
    highlights: str = ""
    weaknesses: str = ""
    learningAdvice: str = ""
