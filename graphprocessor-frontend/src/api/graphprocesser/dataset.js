import request from '@/utils/request'

//获取当前用户的个人所有数据集
export function listMyDataset() {
  return request({
    url: '/graphprocesser/dataset/listMy',
    method: 'get'
  })
}

// 查询数据集列表
export function listDataset(query) {
  return request({
    url: '/graphprocesser/dataset/list',
    method: 'get',
    params: query
  })
}

// 查询数据集详细
export function getDataset(id) {
  return request({
    url: '/graphprocesser/dataset/' + id,
    method: 'get'
  })
}

// 新增数据集
export function addDataset(data) {
  return request({
    url: '/graphprocesser/dataset',
    method: 'post',
    data: data
  })
}

// 修改数据集
export function updateDataset(data) {
  return request({
    url: '/graphprocesser/dataset',
    method: 'put',
    data: data
  })
}

// 删除数据集
export function delDataset(id) {
  return request({
    url: '/graphprocesser/dataset/' + id,
    method: 'delete'
  })
}
