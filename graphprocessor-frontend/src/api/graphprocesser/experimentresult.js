import request from '@/utils/request'

// 查询实验结果列表
export function listExperimentresult(query) {
  return request({
    url: '/graphprocesser/experimentresult/list',
    method: 'get',
    params: query
  })
}

// 查询实验结果详细
export function getExperimentresult(id) {
  return request({
    url: '/graphprocesser/experimentresult/' + id,
    method: 'get'
  })
}

// 新增实验结果
export function addExperimentresult(data) {
  return request({
    url: '/graphprocesser/experimentresult',
    method: 'post',
    data: data
  })
}

// 修改实验结果
export function updateExperimentresult(data) {
  return request({
    url: '/graphprocesser/experimentresult',
    method: 'put',
    data: data
  })
}

// 删除实验结果
export function delExperimentresult(id) {
  return request({
    url: '/graphprocesser/experimentresult/' + id,
    method: 'delete'
  })
}
