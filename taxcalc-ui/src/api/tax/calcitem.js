import request from '@/utils/request'

// 查询税费计算项目列表
export function listCalcitem(query) {
  return request({
    url: '/tax/calcitem/list',
    method: 'get',
    params: query
  })
}

// 查询税费计算项目详细
export function getCalcitem(itemId) {
  return request({
    url: '/tax/calcitem/' + itemId,
    method: 'get'
  })
}

// 查询税费计算项目字段
export function listItemField(itemId) {
  return request({
    url: '/tax/calcitem/fields/' + itemId,
    method: 'get'
  })
}

// 新增税费计算项目
export function addCalcitem(data) {
  return request({
    url: '/tax/calcitem',
    method: 'post',
    data: data
  })
}

// 修改税费计算项目
export function updateCalcitem(data) {
  return request({
    url: '/tax/calcitem',
    method: 'put',
    data: data
  })
}

export function updateItemField(itemId, data){
  return request({
    url: '/tax/calcitem/fields/'+itemId,
    method:'post',
    data:data
  })
}

// 删除税费计算项目
export function delCalcitem(itemId) {
  return request({
    url: '/tax/calcitem/' + itemId,
    method: 'delete'
  })
}

// 导出税费计算项目
export function exportCalcitem(query) {
  return request({
    url: '/tax/calcitem/export',
    method: 'get',
    params: query
  })
}