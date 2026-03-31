import request from '@/utils/request'

// 查询问答对话明细列表
export function listDialogue(query) {
  return request({
    url: '/aip/dialogue/list',
    method: 'get',
    params: query
  })
}

// 查询问答对话明细详细
export function getDialogue(id) {
  return request({
    url: '/aip/dialogue/' + id,
    method: 'get'
  })
}

// 新增问答对话明细
export function addDialogue(data) {
  return request({
    url: '/aip/dialogue',
    method: 'post',
    data: data
  })
}

// 修改问答对话明细
export function updateDialogue(data) {
  return request({
    url: '/aip/dialogue',
    method: 'put',
    data: data
  })
}

// 删除问答对话明细
export function delDialogue(id) {
  return request({
    url: '/aip/dialogue/' + id,
    method: 'delete'
  })
}
