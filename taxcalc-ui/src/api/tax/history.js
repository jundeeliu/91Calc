import request from '@/utils/request'

// 查询用户计算历史列表
export function listHistory(query) {
  return request({
    url: '/tax/history/list',
    method: 'get',
    params: query
  })
}

// 查询用户计算历史详细
export function getHistory(historyId) {
  return request({
    url: '/tax/history/' + historyId,
    method: 'get'
  })
}

// 新增用户计算历史
export function addHistory(data) {
  return request({
    url: '/tax/history',
    method: 'post',
    data: data
  })
}

// 修改用户计算历史
export function updateHistory(data) {
  return request({
    url: '/tax/history',
    method: 'put',
    data: data
  })
}

// 删除用户计算历史
export function delHistory(historyId) {
  return request({
    url: '/tax/history/' + historyId,
    method: 'delete'
  })
}

// 导出用户计算历史
export function exportHistory(query) {
  return request({
    url: '/tax/history/export',
    method: 'get',
    params: query
  })
}