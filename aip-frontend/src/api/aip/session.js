import request from '@/utils/request'

// 查询面试会话流水列表
export function listSession(query) {
  return request({
    url: '/aip/manage/session/list',
    method: 'get',
    params: query
  })
}

// 查询面试会话流水详细
export function getSession(id) {
  return request({
    url: '/aip/manage/session/' + id,
    method: 'get'
  })
}

// 新增面试会话流水
export function addSession(data) {
  return request({
    url: '/aip/manage/session',
    method: 'post',
    data: data
  })
}

// 修改面试会话流水
export function updateSession(data) {
  return request({
    url: '/aip/manage/session',
    method: 'put',
    data: data
  })
}

// 删除面试会话流水
export function delSession(id) {
  return request({
    url: '/aip/manage/session/' + id,
    method: 'delete'
  })
}
