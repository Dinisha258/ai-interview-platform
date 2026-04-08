<template>
  <div class="chat-container">
    <div class="sidebar">
      <div class="sidebar-header">
        <span class="title">面试记录</span>
      </div>
      <div class="session-list">
        <div
          v-for="session in sessionList"
          :key="session.id"
          :class="['session-item', { active: activeSessionId === session.id }]"
          @click="handleSelectSession(session.id)"
        >
          <div class="session-title">{{ session.positionName }} 面试</div>
          <div class="session-time">{{ session.createTime }}</div>
        </div>
      </div>
    </div>

    <div class="main-chat">
      <div class="chat-header">
        <span class="chat-title">
          <i class="el-icon-headset"></i> AI 面试官
        </span>
        <el-button type="danger" size="small" plain icon="el-icon-switch-button" @click="handleEndInterview">
          结束面试
        </el-button>
      </div>

      <div class="chat-box" ref="chatBox">
        <div v-for="(msg, index) in messageList" :key="index" :class="['message-row', msg.role === 'user' ? 'is-user' : 'is-ai']">

          <div class="avatar ai-avatar" v-if="msg.role === 'ai'">AI</div>

          <div class="message-bubble">
            <div class="text-content" v-if="!msg.isThinking" v-html="formatText(msg.content)"></div>

            <div class="thinking-dots" v-else>
              <span></span><span></span><span></span>
            </div>
          </div>

          <div class="avatar user-avatar" v-if="msg.role === 'user'">我</div>
        </div>
      </div>

      <div class="chat-input-area">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="请输入你的回答，或点击左侧语音按钮..."
          v-model="inputContent"
          resize="none"
          @keyup.enter.native="handleSend"
        ></el-input>
        <div class="input-actions">
          <el-button type="info" circle icon="el-icon-mic" title="语音输入(开发中)"></el-button>
          <el-button type="primary" :loading="isAiReplying" @click="handleSend">发送回答 <i class="el-icon-s-promotion"></i></el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "InterviewChat",
  data() {
    return {
      activeSessionId: 1,
      isAiReplying: false, // 控制是否正在等待 AI 响应
      inputContent: "",

      // 模拟会话列表（后续调 /api/interview/session/list 接口替换）
      sessionList: [
        { id: 1, positionName: "Java 后端开发", createTime: "2026-04-01 10:00" },
        { id: 2, positionName: "前端开发", createTime: "2026-03-30 14:30" }
      ],

      // 模拟当前聊天的消息记录
      messageList: [
        { role: 'ai', content: "你好，我是锐捷网络的AI面试官。我已经准备好对你进行技术面了，请先做一个简单的自我介绍吧。", isThinking: false }
      ]
    };
  },
  methods: {
    // 切换会话
    handleSelectSession(id) {
      if (this.isAiReplying) {
        this.$message.warning("AI 正在回复中，请稍后切换会话");
        return;
      }
      this.activeSessionId = id;
      this.$message.info(`切换到了会话: ${id} (这里后续接拉取历史记录接口)`);
      // TODO: 调接口拉取当前 session 的历史记录赋给 this.messageList
    },

    // 发送消息
    async handleSend() {
      // 阻止空发和回车换行造成的空发
      const text = this.inputContent.trim();
      if (!text) return;
      if (this.isAiReplying) return;

      // 1. 把用户的话推入消息列表
      this.messageList.push({ role: 'user', content: text, isThinking: false });
      this.inputContent = "";
      this.scrollToBottom();

      // 2. 锁定状态，推入一个“思考中”的 AI 假消息占位
      this.isAiReplying = true;
      const aiMsgIndex = this.messageList.push({ role: 'ai', content: "", isThinking: true }) - 1;
      this.scrollToBottom();

      // ====== 模拟向后端发送请求 (TODO: 替换为真实的 /api/interview/chat 接口) ======
      await this.mockApiCall();
      // ======================================================================

      // 3. 拿到结果后，取消思考状态，准备打字机效果
      const replyText = "了解了，既然你应聘的是Java岗位，那请你简单谈谈对 Spring Boot 自动装配原理的理解。";
      this.messageList[aiMsgIndex].isThinking = false;

      // 4. 执行打字机动画
      this.playTypewriterEffect(aiMsgIndex, replyText);
    },

    // 模拟接口耗时
    mockApiCall() {
      return new Promise(resolve => setTimeout(resolve, 2000));
    },

    // 打字机特效核心逻辑
    playTypewriterEffect(msgIndex, fullText) {
      let currentIndex = 0;
      let currentText = "";

      const typeInterval = setInterval(() => {
        if (currentIndex < fullText.length) {
          currentText += fullText.charAt(currentIndex);
          // 增量更新消息内容
          this.messageList[msgIndex].content = currentText;
          currentIndex++;
          this.scrollToBottom();
        } else {
          clearInterval(typeInterval);
          this.isAiReplying = false; // 打字完成，解除锁定，允许用户发下一句
        }
      }, 50); // 50ms 敲一个字，可以自己调速度
    },

    // 结束面试
    handleEndInterview() {
      this.$confirm('确定要结束当前的面试吗？结束之后将生成面试报告。', '提示', {
        confirmButtonText: '确定结束',
        cancelButtonText: '继续面试',
        type: 'warning'
      }).then(() => {
        this.$message.success('面试已结束，正在生成报告...');
        // TODO: 调用结束接口，跳转到报告页
      }).catch(() => {});
    },

    // 保持滚动条在最底部
    scrollToBottom() {
      this.$nextTick(() => {
        const box = this.$refs.chatBox;
        if (box) {
          box.scrollTop = box.scrollHeight;
        }
      });
    },

    // 处理文本换行显示
    formatText(text) {
      if (!text) return "";
      return text.replace(/\n/g, "<br>");
    }
  }
};
</script>

<style scoped>
/* 整个容器铺满，采用柔和的背景色 */
.chat-container {
  display: flex;
  height: calc(100vh - 84px); /* 减去若依 Navbar 和 TagsView 的高度 */
  background-color: #f0f2f5;
  padding: 20px;
  box-sizing: border-box;
}

/* 左侧侧边栏 */
.sidebar {
  width: 280px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
  margin-right: 20px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.sidebar-header {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
  font-weight: bold;
  font-size: 16px;
  color: #303133;
}

.session-list {
  flex: 1;
  overflow-y: auto;
}

.session-item {
  padding: 15px 20px;
  border-bottom: 1px solid #fafafa;
  cursor: pointer;
  transition: all 0.3s;
}

.session-item:hover {
  background-color: #f5f7fa;
}

.session-item.active {
  background-color: #ecf5ff;
  border-left: 4px solid #409EFF;
}

.session-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 5px;
}

.session-time {
  font-size: 12px;
  color: #909399;
}

/* 右侧主聊天区 */
.main-chat {
  flex: 1;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-header {
  height: 60px;
  padding: 0 20px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

/* 消息滚动区 */
.chat-box {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f7f8fa;
}

.message-row {
  display: flex;
  margin-bottom: 20px;
}

.message-row.is-user {
  justify-content: flex-end;
}

.message-row.is-ai {
  justify-content: flex-start;
}

/* 头像 */
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
  font-size: 14px;
  flex-shrink: 0;
}

.ai-avatar {
  background-color: #1890ff;
  color: white;
  margin-right: 15px;
}

.user-avatar {
  background-color: #67C23A;
  color: white;
  margin-left: 15px;
}

/* 气泡样式 */
.message-bubble {
  max-width: 60%;
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.6;
  word-wrap: break-word;
}

.is-user .message-bubble {
  background-color: #95ec69; /* 微信绿 */
  color: #333;
  border-top-right-radius: 2px;
}

.is-ai .message-bubble {
  background-color: #fff;
  color: #333;
  border: 1px solid #ebeef5;
  border-top-left-radius: 2px;
}

/* 输入区 */
.chat-input-area {
  padding: 15px 20px;
  border-top: 1px solid #ebeef5;
  background-color: #fff;
}

.input-actions {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* === AI 思考中的动态省略号动画 === */
.thinking-dots {
  display: flex;
  align-items: center;
  height: 22px;
}

.thinking-dots span {
  display: inline-block;
  width: 6px;
  height: 6px;
  margin: 0 2px;
  background-color: #909399;
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out both;
}

.thinking-dots span:nth-child(1) { animation-delay: -0.32s; }
.thinking-dots span:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
  0%, 80%, 100% { transform: scale(0); opacity: 0.5; }
  40% { transform: scale(1); opacity: 1; }
}
</style>
