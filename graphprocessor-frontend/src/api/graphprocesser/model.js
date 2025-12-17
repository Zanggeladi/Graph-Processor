import request from '@/utils/request'


//获取当前用户的所有模型
export function listMyModel() {
  return request({
    url: '/graphprocesser/model/listMy',
    method: 'get'
  })
}

// 查询模型列表
export function listModel(query) {
  return request({
    url: '/graphprocesser/model/list',
    method: 'get',
    params: query
  })
}

// 查询模型详细
export function getModel(id) {
  return request({
    url: '/graphprocesser/model/' + id,
    method: 'get'
  })
}

// 新增模型
export function addModel(data) {
  return request({
    url: '/graphprocesser/model',
    method: 'post',
    data: data
  })
}

// 修改模型
export function updateModel(data) {
  return request({
    url: '/graphprocesser/model',
    method: 'put',
    data: data
  })
}

// 删除模型
export function delModel(id) {
  return request({
    url: '/graphprocesser/model/' + id,
    method: 'delete'
  })
}
