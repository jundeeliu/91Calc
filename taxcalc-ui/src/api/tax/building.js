import request from '@/utils/request'

// 查询楼栋信息列表
export function listBuilding(query) {
  return request({
    url: '/tax/building/list',
    method: 'get',
    params: query
  })
}

// 查询楼栋信息详细
export function getBuilding(buildingId) {
  return request({
    url: '/tax/building/' + buildingId,
    method: 'get'
  })
}

// 新增楼栋信息
export function addBuilding(data) {
  return request({
    url: '/tax/building',
    method: 'post',
    data: data
  })
}

// 修改楼栋信息
export function updateBuilding(data) {
  return request({
    url: '/tax/building',
    method: 'put',
    data: data
  })
}

// 删除楼栋信息
export function delBuilding(buildingId) {
  return request({
    url: '/tax/building/' + buildingId,
    method: 'delete'
  })
}

// 导出楼栋信息
export function exportBuilding(query) {
  return request({
    url: '/tax/building/export',
    method: 'get',
    params: query
  })
}