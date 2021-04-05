import request from '@/utils/request'

// 查询税费计算项目组列表
export function listCalcitemgroup(query) {
  return request({
    url: '/tax/calcitemgroup/list',
    method: 'get',
    params: query
  })
}

// 查询税费计算项目组详细
export function getCalcitemgroup(groupId) {
  return request({
    url: '/tax/calcitemgroup/' + groupId,
    method: 'get'
  })
}

// 新增税费计算项目组
export function addCalcitemgroup(data) {
  return request({
    url: '/tax/calcitemgroup',
    method: 'post',
    data: data
  })
}

// 修改税费计算项目组
export function updateCalcitemgroup(data) {
  return request({
    url: '/tax/calcitemgroup',
    method: 'put',
    data: data
  })
}


// 删除税费计算项目组
export function delCalcitemgroup(groupId) {
  return request({
    url: '/tax/calcitemgroup/' + groupId,
    method: 'delete'
  })
}

// 导出税费计算项目组
export function exportCalcitemgroup(query) {
  return request({
    url: '/tax/calcitemgroup/export',
    method: 'get',
    params: query
  })
}

// 查询项目组下拉树结构
export function treeselect() {
  return request({
    url: '/tax/calcitemgroup/treeselect',
    method: 'get'
  })
}