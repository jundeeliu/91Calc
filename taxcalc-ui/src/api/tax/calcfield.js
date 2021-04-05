import request from '@/utils/request'

// 查询税费计算字段列表
export function listCalcfield(query) {
  return request({
    url: '/tax/calcfield/list',
    method: 'get',
    params: query
  })
}

// 查询税费计算字段详细
export function getCalcfield(fieldId) {
  return request({
    url: '/tax/calcfield/' + fieldId,
    method: 'get'
  })
}

// 新增税费计算字段
export function addCalcfield(data) {
  return request({
    url: '/tax/calcfield',
    method: 'post',
    data: data
  })
}

// 修改税费计算字段
export function updateCalcfield(data) {
  return request({
    url: '/tax/calcfield',
    method: 'put',
    data: data
  })
}

// 删除税费计算字段
export function delCalcfield(fieldId) {
  return request({
    url: '/tax/calcfield/' + fieldId,
    method: 'delete'
  })
}

// 导出税费计算字段
export function exportCalcfield(query) {
  return request({
    url: '/tax/calcfield/export',
    method: 'get',
    params: query
  })
}