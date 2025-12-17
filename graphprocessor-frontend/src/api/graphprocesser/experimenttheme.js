import request from '@/utils/request'

// 查询对比实验主题列表
export function listExperimenttheme(query) {
  return request({
    url: '/graphprocesser/experimenttheme/list',
    method: 'get',
    params: query
  })
}

// 查询对比实验主题详细
export function getExperimenttheme(id) {
  return request({
    url: '/graphprocesser/experimenttheme/' + id,
    method: 'get'
  })
}

// 新增对比实验主题
export function addExperimenttheme(data) {
  return request({
    url: '/graphprocesser/experimenttheme',
    method: 'post',
    data: data
  })
}

// 修改对比实验主题
export function updateExperimenttheme(data) {
  return request({
    url: '/graphprocesser/experimenttheme',
    method: 'put',
    data: data
  })
}

// 删除对比实验主题
export function delExperimenttheme(id) {
  return request({
    url: '/graphprocesser/experimenttheme/' + id,
    method: 'delete'
  })
}
