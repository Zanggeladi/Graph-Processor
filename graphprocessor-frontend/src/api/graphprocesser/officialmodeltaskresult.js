import request from '@/utils/request'

// 查询官方模型图数据处理任务结果列表
export function listOfficialmodeltaskresult(query) {
  return request({
    url: '/graphprocesser/officialmodeltaskresult/list',
    method: 'get',
    params: query
  })
}

// 查询官方模型图数据处理任务结果详细bytaskid
export function getOfficialmodeltaskresultByTaskId(taskId) {
  return request({
    url: '/graphprocesser/officialmodeltaskresult/taskId/' + taskId,
    method: 'get'
  })
}

// 查询官方模型图数据处理任务结果详细
export function getOfficialmodeltaskresult(id) {
  return request({
    url: '/graphprocesser/officialmodeltaskresult/' + id,
    method: 'get'
  })
}

// 新增官方模型图数据处理任务结果
export function addOfficialmodeltaskresult(data) {
  return request({
    url: '/graphprocesser/officialmodeltaskresult',
    method: 'post',
    data: data
  })
}

// 修改官方模型图数据处理任务结果
export function updateOfficialmodeltaskresult(data) {
  return request({
    url: '/graphprocesser/officialmodeltaskresult',
    method: 'put',
    data: data
  })
}

// 删除官方模型图数据处理任务结果
export function delOfficialmodeltaskresult(id) {
  return request({
    url: '/graphprocesser/officialmodeltaskresult/' + id,
    method: 'delete'
  })
}
