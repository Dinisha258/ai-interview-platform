import request from '@/utils/request'

// 查询岗位分类列表
export function listPosition(query) {
  return request({
    url: '/aip/position/list',
    method: 'get',
    params: query
  })
}

// 查询岗位分类详细
export function getPosition(id) {
  return request({
    url: '/aip/position/' + id,
    method: 'get'
  })
}

// 新增岗位分类
export function addPosition(data) {
  return request({
    url: '/aip/position',
    method: 'post',
    data: data
  })
}

// 修改岗位分类
export function updatePosition(data) {
  return request({
    url: '/aip/position',
    method: 'put',
    data: data
  })
}

// 删除岗位分类
export function delPosition(id) {
  return request({
    url: '/aip/position/' + id,
    method: 'delete'
  })
}
