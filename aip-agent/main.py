import uvicorn
from fastapi import FastAPI

app = FastAPI(title="AI Interview Agent")

@app.get("/")
async def root():
    return {"message": "Agent 微服务初始化成功！"}

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
