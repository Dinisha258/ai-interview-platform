from pydantic import BaseModel
from typing import List, Optional

# 定义单条历史记录的格式
class Message(BaseModel):
    role: str  # "ai" 或 "user"
    content: str

# Spring Boot 传过来的总包
class InterviewRequest(BaseModel):
    session_id: int
    current_input: str         # 学生这一轮说的话
    history: List[Message]      # 之前几轮的记录
    position_name: str         # 岗位名称
    target_level: int          # 难度等级
    is_audio: bool = False     # 是否是语音输入
