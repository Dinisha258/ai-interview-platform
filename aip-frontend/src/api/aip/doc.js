import request from '@/utils/request'

// 查询知识库文档列表
export function listDoc(query) {
  return request({
    url: '/aip/manage/doc/list',
    method: 'get',
    params: query
  })
}

// 查询知识库文档详细
export function getDoc(id) {
  return request({
    url: '/aip/manage/doc/' + id,
    method: 'get'
  })
}

// 新增知识库文档
export function addDoc(data) {
  return request({
    url: '/aip/manage/doc',
    method: 'post',
    data: data
  })
}

// 修改知识库文档
export function updateDoc(data) {
  return request({
    url: '/aip/manage/doc',
    method: 'put',
    data: data
  })
}

// 删除知识库文档
export function delDoc(id) {
  return request({
    url: '/aip/manage/doc/' + id,
    method: 'delete'
  })
}
