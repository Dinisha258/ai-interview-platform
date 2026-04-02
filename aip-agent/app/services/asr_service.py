import os
import httpx
import tempfile
import asyncio
import dashscope
from dashscope.audio.asr import Recognition

# 设置阿里云百炼的 API KEY
dashscope.api_key = os.getenv("ALIYUN_API_KEY")

async def transcribe_audio(audio_url: str) -> str:
    """
    异步下载音频并调用阿里云 FunASR (SenseVoice) 进行语音转文字
    """
    if not audio_url:
        return ""

    print(f"[ASR] 开始处理音频: {audio_url}")

    # 1. 异步下载 MinIO 里的音频文件
    async with httpx.AsyncClient() as client:
        response = await client.get(audio_url)
        if response.status_code != 200:
            raise Exception(f"无法从 MinIO 下载音频，状态码: {response.status_code}")
        audio_bytes = response.content

    # 2. 存入临时文件 (因为 DashScope SDK 读本地文件最稳定)
    tmp_fd, tmp_path = tempfile.mkstemp(suffix=".wav")
    try:
        with os.fdopen(tmp_fd, 'wb') as f:
            f.write(audio_bytes)

        # 3. 调用阿里云 ASR (SenseVoice-v1 是目前识别最快、支持多语种的模型)
        # 因为 SDK 的 Recognition.call 是同步阻塞的，我们用线程池把它包起来，防止卡死 FastAPI
        loop = asyncio.get_event_loop()

        def _call_aliyun_asr():
            return Recognition.call(
                model='sensevoice-v1',
                file=tmp_path
            )

        result = await loop.run_in_executor(None, _call_aliyun_asr)

        # 4. 解析结果
        if result.status_code == 200:
            # SenseVoice 的返回格式中，文本一般在 output.text 中
            text = result.get_output().get("text", "")
            print(f"[ASR] 识别成功: {text}")
            return text
        else:
            print(f"[ASR] 识别失败: {result.message}")
            return ""

    finally:
        # 5. 极其重要：处理完必须删除临时文件，防止把服务器硬盘塞满！
        if os.path.exists(tmp_path):
            os.remove(tmp_path)
