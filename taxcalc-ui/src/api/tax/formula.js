import request from '@/utils/request'

// 查询计算公式列表
export function listFormula(query) {
  return request({
    url: '/tax/formula/list',
    method: 'get',
    params: query
  })
}

// 查询计算公式详细
export function getFormula(formulaId) {
  return request({
    url: '/tax/formula/' + formulaId,
    method: 'get'
  })
}

// 新增计算公式
export function addFormula(data) {
  return request({
    url: '/tax/formula',
    method: 'post',
    data: data
  })
}

// 修改计算公式
export function updateFormula(data) {
  return request({
    url: '/tax/formula',
    method: 'put',
    data: data
  })
}

// 删除计算公式
export function delFormula(formulaId) {
  return request({
    url: '/tax/formula/' + formulaId,
    method: 'delete'
  })
}

// 导出计算公式
export function exportFormula(query) {
  return request({
    url: '/tax/formula/export',
    method: 'get',
    params: query
  })
}

export function listItemFields(itemId){
  return request({
    url: '/tax/formula/itemfields/'+itemId,
    method:'get'
  })
}