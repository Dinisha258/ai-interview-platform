<template>
  <div class="chat-container">
    <div class="sidebar">
      <div class="sidebar-header">
        <span class="title">面试记录</span>
        <el-button type="primary" size="mini" icon="el-icon-plus" @click="openNewDialog">新面试</el-button>
      </div>
      <div class="session-list">
        <div
          v-for="session in sessionList"
          :key="session.id"
          :class="['session-item', { active: activeSessionId === session.id }]"
          @click="handleSelectSession(session.id)"
        >
          <div class="session-title">{{ session.positionName || '未知岗位' }} 面试</div>
          <div class="session-time">{{ session.createTime }}
            <el-tag v-if="session.status === 0" size="mini" type="success">进行中</el-tag>
            <el-tag v-else size="mini" type="info">已结束</el-tag>
          </div>
        </div>
      </div>
    </div>

    <div class="main-chat">
      <div class="chat-header">
        <span class="chat-title">
          <i class="el-icon-headset"></i> AI 面试官
        </span>
        <el-button type="danger" size="small" plain icon="el-icon-switch-button" @click="handleEndInterview" :disabled="!isSessionActive">
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
          <el-tooltip :content="isRecording ? '点击停止录音并发送' : '点击开始语音输入'" placement="top">
            <el-button
              :type="isRecording ? 'danger' : 'info'"
              circle
              :icon="isRecording ? 'el-icon-turn-off-microphone' : 'el-icon-mic'"
              @click="handleVoiceInput"
              :disabled="isAiReplying || !isSessionActive"
            ></el-button>
          </el-tooltip>
          <span v-if="isRecording" class="recording-hint">录音中... {{ recordingSeconds }}s</span>
          <el-button type="primary" :loading="isAiReplying" @click="handleSend" :disabled="!isSessionActive">发送回答 <i class="el-icon-s-promotion"></i></el-button>
        </div>
      </div>
    </div>

    <!-- 新建面试对话框 -->
    <el-dialog title="开始新面试" :visible.sync="showNewDialog" width="420px" :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-form :model="newForm" label-width="80px">
        <el-form-item label="面试岗位">
          <el-select v-model="newForm.positionId" placeholder="请选择岗位" style="width: 100%">
            <el-option v-for="p in positionOptions" :key="p.id" :label="p.positionName" :value="p.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标等级">
          <el-select v-model="newForm.targetLevel" placeholder="请选择等级" style="width: 100%">
            <el-option :value="1" label="实习生"></el-option>
            <el-option :value="2" label="初级"></el-option>
            <el-option :value="3" label="中级"></el-option>
            <el-option :value="4" label="高级"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" :loading="newLoading" @click="handleStartInterview">开始面试</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { startInterview, interviewChat, endInterview, listMySessions, getDialogues, uploadAudio } from "@/api/aip/interview";
import { listPosition } from "@/api/aip/position";

export default {
  name: "InterviewChat",
  data() {
    return {
      activeSessionId: null,
      isAiReplying: false,
      inputContent: "",
      roundNum: 0,
      sessionList: [],
      messageList: [],
      // 新建面试
      showNewDialog: false,
      newLoading: false,
      newForm: { positionId: null, targetLevel: null },
      positionOptions: [],
      // 语音录制
      isRecording: false,
      recordingSeconds: 0,
      mediaRecorder: null,
      audioChunks: [],
      recordingTimer: null
    };
  },
  computed: {
    activeSession() {
      return this.sessionList.find(s => s.id === this.activeSessionId);
    },
    isSessionActive() {
      return this.activeSession && this.activeSession.status === 0;
    }
  },
  created() {
    this.loadSessions();
  },
  beforeDestroy() {
    this.stopRecording();
  },
  methods: {
    // 加载当前用户的会话列表
    async loadSessions() {
      try {
        const res = await listMySessions();
        this.sessionList = res.data || [];
        // 优先选中进行中的会话
        const activeSession = this.sessionList.find(s => s.status === 0);
        if (activeSession) {
          this.activeSessionId = activeSession.id;
          this.loadDialogues(activeSession.id);
        } else {
          // 没有进行中的会话，弹出新建对话框
          this.openNewDialog();
        }
      } catch (e) {
        console.error("加载会话列表失败", e);
      }
    },

    // 打开新建面试对话框
    async openNewDialog() {
      this.newForm = { positionId: null, targetLevel: null };
      this.showNewDialog = true;
      try {
        const res = await listPosition();
        this.positionOptions = res.rows || [];
      } catch (e) {
        this.$message.error("加载岗位列表失败");
      }
    },

    // 开始新面试
    async handleStartInterview() {
      if (!this.newForm.positionId) {
        this.$message.warning("请选择面试岗位");
        return;
      }
      if (!this.newForm.targetLevel) {
        this.$message.warning("请选择目标等级");
        return;
      }
      this.newLoading = true;
      try {
        const res = await startInterview(this.newForm);
        this.showNewDialog = false;
        this.$message.success("面试已开始");
        // 重新加载会话列表并选中新会话
        const listRes = await listMySessions();
        this.sessionList = listRes.data || [];
        const newId = res.data;
        this.activeSessionId = newId;
        this.loadDialogues(newId);
      } catch (e) {
        this.$message.error("创建面试失败，请重试");
        console.error("创建面试失败", e);
      } finally {
        this.newLoading = false;
      }
    },

    // 加载指定会话的对话记录
    async loadDialogues(sessionId) {
      try {
        const res = await getDialogues(sessionId);
        const dialogues = res.data || [];
        this.messageList = [];
        this.roundNum = 0;
        // 静态开场白
        this.messageList.push({
          role: 'ai',
          content: "你好，我是AI面试官。我已经准备好对你进行技术面了，请先做一个简单的自我介绍吧。",
          isThinking: false
        });
        // 还原历史对话
        dialogues.forEach(d => {
          if (d.userContent) {
            this.messageList.push({ role: 'user', content: d.userContent, isThinking: false });
          }
          if (d.aiContent) {
            this.messageList.push({ role: 'ai', content: d.aiContent, isThinking: false });
          }
          if (d.roundNum != null && d.roundNum > this.roundNum) {
            this.roundNum = d.roundNum;
          }
        });
        this.scrollToBottom();
      } catch (e) {
        console.error("加载对话记录失败", e);
      }
    },

    // 切换会话
    handleSelectSession(id) {
      if (this.isAiReplying) {
        this.$message.warning("AI 正在回复中，请稍后切换会话");
        return;
      }
      this.activeSessionId = id;
      this.loadDialogues(id);
    },

    // 发送文字消息
    async handleSend() {
      const text = this.inputContent.trim();
      if (!text) return;
      if (this.isAiReplying) return;
      if (!this.isSessionActive) {
        this.$message.warning("该会话已结束，无法继续发送消息");
        return;
      }

      this.messageList.push({ role: 'user', content: text, isThinking: false });
      this.inputContent = "";
      this.scrollToBottom();

      await this.sendToAgent({ textContent: text });
    },

    // 语音输入
    async handleVoiceInput() {
      if (this.isRecording) {
        this.stopRecording();
      } else {
        this.startRecording();
      }
    },

    // 开始录音
    async startRecording() {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        this.audioChunks = [];
        this.mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/webm' });

        this.mediaRecorder.ondataavailable = (e) => {
          if (e.data.size > 0) {
            this.audioChunks.push(e.data);
          }
        };

        this.mediaRecorder.onstop = async () => {
          // 停止所有音轨
          stream.getTracks().forEach(t => t.stop());

          if (this.audioChunks.length === 0) return;

          const audioBlob = new Blob(this.audioChunks, { type: 'audio/webm' });
          await this.sendVoiceMessage(audioBlob);
        };

        this.mediaRecorder.start();
        this.isRecording = true;
        this.recordingSeconds = 0;
        this.recordingTimer = setInterval(() => {
          this.recordingSeconds++;
          // 最长录音 120 秒自动停止
          if (this.recordingSeconds >= 120) {
            this.stopRecording();
          }
        }, 1000);
      } catch (e) {
        this.$message.error("无法访问麦克风，请检查浏览器权限设置");
        console.error("麦克风访问失败", e);
      }
    },

    // 停止录音
    stopRecording() {
      if (this.recordingTimer) {
        clearInterval(this.recordingTimer);
        this.recordingTimer = null;
      }
      if (this.mediaRecorder && this.mediaRecorder.state !== 'inactive') {
        this.mediaRecorder.stop();
      }
      this.isRecording = false;
    },

    // 发送语音消息
    async sendVoiceMessage(audioBlob) {
      this.messageList.push({ role: 'user', content: '🎤 语音消息 (' + this.recordingSeconds + 's)', isThinking: false });
      this.scrollToBottom();

      try {
        // 上传音频到 MinIO
        const audioFile = new File([audioBlob], 'recording_' + Date.now() + '.webm', { type: 'audio/webm' });
        const uploadRes = await uploadAudio(audioFile);
        const audioUrl = uploadRes.url;

        await this.sendToAgent({ audioUrl: audioUrl });
      } catch (e) {
        this.$message.error("语音上传失败，请重试");
        console.error("语音上传失败", e);
        // 移除语音消息占位
        this.messageList.pop();
      }
    },

    // 统一发送到 Agent
    async sendToAgent(params) {
      this.isAiReplying = true;
      const aiMsgIndex = this.messageList.push({ role: 'ai', content: "", isThinking: true }) - 1;
      this.scrollToBottom();

      this.roundNum++;

      try {
        const res = await interviewChat({
          sessionId: this.activeSessionId,
          roundNum: this.roundNum,
          textContent: params.textContent || null,
          audioUrl: params.audioUrl || null
        });
        const data = res.data;
        this.messageList[aiMsgIndex].isThinking = false;

        // 播放 TTS 语音
        this.playTtsAudio(data.ttsAudioBase64);

        this.playTypewriterEffect(aiMsgIndex, data.aiReply, () => {
          if (data.isFinished) {
            this.doEndInterview();
          }
        });
      } catch (e) {
        this.messageList[aiMsgIndex].isThinking = false;
        this.messageList[aiMsgIndex].content = "抱歉，AI 思考时出现了问题，请重试。";
        this.isAiReplying = false;
        this.roundNum--;
      }
    },

    // 播放 TTS 音频
    playTtsAudio(base64Audio) {
      if (!base64Audio) return;
      try {
        const audio = new Audio('data:audio/wav;base64,' + base64Audio);
        audio.play().catch(e => console.warn("TTS 自动播放被浏览器拦截", e));
      } catch (e) {
        console.warn("TTS 播放失败", e);
      }
    },

    // 打字机特效核心逻辑
    playTypewriterEffect(msgIndex, fullText, onComplete) {
      let currentIndex = 0;
      let currentText = "";

      const typeInterval = setInterval(() => {
        if (currentIndex < fullText.length) {
          currentText += fullText.charAt(currentIndex);
          this.messageList[msgIndex].content = currentText;
          currentIndex++;
          this.scrollToBottom();
        } else {
          clearInterval(typeInterval);
          this.isAiReplying = false;
          if (onComplete) onComplete();
        }
      }, 50);
    },

    // 结束面试
    handleEndInterview() {
      if (!this.isSessionActive) {
        this.$message.info("该会话已结束");
        return;
      }
      this.$confirm('确定要结束当前的面试吗？结束之后将生成面试报告。', '提示', {
        confirmButtonText: '确定结束',
        cancelButtonText: '继续面试',
        type: 'warning'
      }).then(() => {
        this.doEndInterview();
      }).catch(() => {});
    },

    // 调用结束接口
    async doEndInterview() {
      try {
        await endInterview(this.activeSessionId);
        this.$message.success("面试已结束");
        this.loadSessions();
      } catch (e) {
        console.error("结束面试失败", e);
      }
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
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.recording-hint {
  color: #F56C6C;
  font-size: 13px;
  margin-left: 10px;
  animation: blink 1s infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
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
