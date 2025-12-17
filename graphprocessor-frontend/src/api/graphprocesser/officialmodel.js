import request from '@/utils/request'

//查询所有官方模型
export function listAllOfficialmodel() {
  return request({
    url: '/graphprocesser/officialmodel/list/all',
    method: 'get'
  })
}

// 查询官方模型列表
export function listOfficialmodel(query) {
  return request({
    url: '/graphprocesser/officialmodel/list',
    method: 'get',
    params: query
  })
}

// 查询官方模型详细
export function getOfficialmodel(id) {
  return request({
    url: '/graphprocesser/officialmodel/' + id,
    method: 'get'
  })
}

// 新增官方模型
export function addOfficialmodel(data) {
  return request({
    url: '/graphprocesser/officialmodel',
    method: 'post',
    data: data
  })
}

// 修改官方模型
export function updateOfficialmodel(data) {
  return request({
    url: '/graphprocesser/officialmodel',
    method: 'put',
    data: data
  })
}

// 删除官方模型
export function delOfficialmodel(id) {
  return request({
    url: '/graphprocesser/officialmodel/' + id,
    method: 'delete'
  })
}
