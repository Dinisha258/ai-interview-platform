import request from '@/utils/request'

// 开始面试
export function startInterview(data) {
  return request({
    url: '/api/control/start',
    method: 'post',
    data: data
  })
}

// 面试问答
export function interviewChat(data) {
  return request({
    url: '/api/control/chat',
    method: 'post',
    data: data,
    timeout: 60000
  })
}

// 结束面试
export function endInterview(sessionId) {
  return request({
    url: '/api/control/end/' + sessionId,
    method: 'post'
  })
}

// 获取当前用户的面试会话列表
export function listMySessions() {
  return request({
    url: '/api/control/sessions',
    method: 'get'
  })
}

// 获取指定会话的对话记录
export function getDialogues(sessionId) {
  return request({
    url: '/api/control/dialogues/' + sessionId,
    method: 'get'
  })
}

// 上传音频文件到 MinIO
export function uploadAudio(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/api/file/upload',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 30000
  })
}
