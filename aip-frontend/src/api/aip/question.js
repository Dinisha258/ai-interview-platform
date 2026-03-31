import request from '@/utils/request'

// 查询面试题目库列表
export function listQuestion(query) {
  return request({
    url: '/aip/question/list',
    method: 'get',
    params: query
  })
}

// 查询面试题目库详细
export function getQuestion(id) {
  return request({
    url: '/aip/question/' + id,
    method: 'get'
  })
}

// 新增面试题目库
export function addQuestion(data) {
  return request({
    url: '/aip/question',
    method: 'post',
    data: data
  })
}

// 修改面试题目库
export function updateQuestion(data) {
  return request({
    url: '/aip/question',
    method: 'put',
    data: data
  })
}

// 删除面试题目库
export function delQuestion(id) {
  return request({
    url: '/aip/question/' + id,
    method: 'delete'
  })
}
