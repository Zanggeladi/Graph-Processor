import request from '@/utils/request'
import * as querystring from "node:querystring";


// 提交任务到消息队列
export function submitExperimentTask(data) {
  return request({
    url: '/graphprocesser/experimenttask/submit',
    method: 'post',
    data: data
  })
}

//获取某个主题对应的所有任务
export function listExperimenttaskByTheme(id, query) {
  return request({
    url: '/graphprocesser/experimenttask/list/' + id,
    method: 'get',
    params: query
  })
}

// 查询对比实验任务列表
export function listExperimenttask(query) {
  return request({
    url: '/graphprocesser/experimenttask/list',
    method: 'get',
    params: query
  })
}

// 查询对比实验任务详细
export function getExperimenttask(id) {
  return request({
    url: '/graphprocesser/experimenttask/' + id,
    method: 'get'
  })
}

// 新增对比实验任务
export function addExperimenttask(data) {
  return request({
    url: '/graphprocesser/experimenttask',
    method: 'post',
    data: data
  })
}

// 修改对比实验任务
export function updateExperimenttask(data) {
  return request({
    url: '/graphprocesser/experimenttask',
    method: 'put',
    data: data
  })
}

// 删除对比实验任务
export function delExperimenttask(id) {
  return request({
    url: '/graphprocesser/experimenttask/' + id,
    method: 'delete'
  })
}
