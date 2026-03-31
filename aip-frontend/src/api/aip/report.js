import request from '@/utils/request'

// 查询综合评估报告列表
export function listReport(query) {
  return request({
    url: '/aip/report/list',
    method: 'get',
    params: query
  })
}

// 查询综合评估报告详细
export function getReport(id) {
  return request({
    url: '/aip/report/' + id,
    method: 'get'
  })
}

// 新增综合评估报告
export function addReport(data) {
  return request({
    url: '/aip/report',
    method: 'post',
    data: data
  })
}

// 修改综合评估报告
export function updateReport(data) {
  return request({
    url: '/aip/report',
    method: 'put',
    data: data
  })
}

// 删除综合评估报告
export function delReport(id) {
  return request({
    url: '/aip/report/' + id,
    method: 'delete'
  })
}
