import request from '@/utils/request'

// submitTaskAPI, getTaskStatus
export function submitTaskAPI(data) {
  return request({
    url: '/graphprocesser/officialmodeltask/submit',
    method: 'post',
    data: data
  })
}
// 获取任务状态
export function getTaskStatus(id) {
  return request({
    url: '/graphprocesser/officialmodeltask/status/' + id,
    method: 'get'
  });
}

// 查询个人的官方模型实验任务列表
export function listMyOfficialmodeltask(query) {
  return request({
    url: '/graphprocesser/officialmodeltask/list/my',
    method: 'get',
    params: query
  })
}

// 查询官方模型实验任务列表
export function listOfficialmodeltask(query) {
  return request({
    url: '/graphprocesser/officialmodeltask/list',
    method: 'get',
    params: query
  })
}

// 查询官方模型实验任务详细
export function getOfficialmodeltask(id) {
  return request({
    url: '/graphprocesser/officialmodeltask/' + id,
    method: 'get'
  })
}

// 新增官方模型实验任务
export function addOfficialmodeltask(data) {
  return request({
    url: '/graphprocesser/officialmodeltask',
    method: 'post',
    data: data
  })
}

// 修改官方模型实验任务
export function updateOfficialmodeltask(data) {
  return request({
    url: '/graphprocesser/officialmodeltask',
    method: 'put',
    data: data
  })
}

// 删除官方模型实验任务
export function delOfficialmodeltask(id) {
  return request({
    url: '/graphprocesser/officialmodeltask/' + id,
    method: 'delete'
  })
}
