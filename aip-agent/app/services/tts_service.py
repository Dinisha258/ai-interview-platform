import os
import base64
import asyncio
import dashscope
from dashscope.audio.tts import SpeechSynthesizer

dashscope.api_key = os.getenv("ALIYUN_API_KEY")

async def synthesize_text(text: str) -> str:
    """
    异步调用阿里云 Qwen3-TTS-Flash，将文本转为 Base64 音频流
    """
    if not text:
        return ""

    print(f"[TTS] 开始合成语音: {text[:20]}...")

    loop = asyncio.get_event_loop()

    def _call_aliyun_tts():
        # 调用百炼的语音合成接口
        return SpeechSynthesizer.call(
            model='qwen3-tts-flash', # 使用你指定的极速模型
            text=text,
            sample_rate=16000,
            format='wav' # 网页端最容易兼容的格式
        )

    result = await loop.run_in_executor(None, _call_aliyun_tts)

    if result.get_audio_data() is not None:
        # 直接拿内存里的字节流，不写硬盘
        audio_bytes = result.get_audio_data()
        audio_b64 = base64.b64encode(audio_bytes).decode('utf-8')
        print("[TTS] 合成成功，已转为 Base64")
        return audio_b64
    else:
        print(f"[TTS] 合成失败: {result.message}")
        return ""
