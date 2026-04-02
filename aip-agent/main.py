import uvicorn
from fastapi import FastAPI
from dotenv import load_dotenv

# 必须在导入任何需要环境变量的包之前加载 .env
load_dotenv()

from app.api import router

app = FastAPI(title="AIP AI Agent")

# 将 api.py 里的路由挂载到主程序，前缀对标 Java 调用的 url
app.include_router(router, prefix="/api/agent")

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
