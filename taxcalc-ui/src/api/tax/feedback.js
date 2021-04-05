import request from '@/utils/request'

// 查询问题反馈列表
export function listFeedback(query) {
  return request({
    url: '/tax/feedback/list',
    method: 'get',
    params: query
  })
}

// 查询问题反馈详细
export function getFeedback(feedbackId) {
  return request({
    url: '/tax/feedback/' + feedbackId,
    method: 'get'
  })
}

// 新增问题反馈
export function addFeedback(data) {
  return request({
    url: '/tax/feedback',
    method: 'post',
    data: data
  })
}

// 修改问题反馈
export function updateFeedback(data) {
  return request({
    url: '/tax/feedback',
    method: 'put',
    data: data
  })
}

// 删除问题反馈
export function delFeedback(feedbackId) {
  return request({
    url: '/tax/feedback/' + feedbackId,
    method: 'delete'
  })
}

// 导出问题反馈
export function exportFeedback(query) {
  return request({
    url: '/tax/feedback/export',
    method: 'get',
    params: query
  })
}