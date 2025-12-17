import request from '@/utils/request'

// 查询审核数据集列表
export function listReviewdataset(query) {
  return request({
    url: '/graphprocesser/reviewdataset/list',
    method: 'get',
    params: query
  })
}

// 查询审核数据集详细
export function getReviewdataset(datasetId) {
  return request({
    url: '/graphprocesser/reviewdataset/' + datasetId,
    method: 'get'
  })
}

// 新增审核数据集
export function addReviewdataset(data) {
  return request({
    url: '/graphprocesser/reviewdataset',
    method: 'post',
    data: data
  })
}

// 修改审核数据集
export function updateReviewdataset(data) {
  return request({
    url: '/graphprocesser/reviewdataset',
    method: 'put',
    data: data
  })
}

// 删除审核数据集
export function delReviewdataset(datasetId) {
  return request({
    url: '/graphprocesser/reviewdataset/' + datasetId,
    method: 'delete'
  })
}
