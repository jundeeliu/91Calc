import request from '@/utils/request'

// 查询上报地税评估列表
export function listReport(query) {
  return request({
    url: '/tax/report/list',
    method: 'get',
    params: query
  })
}

// 查询上报地税评估详细
export function getReport(reportId) {
  return request({
    url: '/tax/report/' + reportId,
    method: 'get'
  })
}

// 新增上报地税评估
export function addReport(data) {
  return request({
    url: '/tax/report',
    method: 'post',
    data: data
  })
}

// 修改上报地税评估
export function updateReport(data) {
  return request({
    url: '/tax/report',
    method: 'put',
    data: data
  })
}

// 删除上报地税评估
export function delReport(reportId) {
  return request({
    url: '/tax/report/' + reportId,
    method: 'delete'
  })
}

// 导出上报地税评估
export function exportReport(query) {
  return request({
    url: '/tax/report/export',
    method: 'get',
    params: query
  })
}

// 审核上报地税评估
export function auditReport(reportId) {
  return request({
    url: '/tax/report/audit/'+reportId,
    method: 'post'
  })
}