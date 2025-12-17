package com.ruoyi.graphprocesser.service;

import java.util.List;
import com.ruoyi.graphprocesser.domain.ExperimentTask;

/**
 * 对比实验任务Service接口
 * 
 * @author zgs
 * @date 2024-10-27
 */
public interface IExperimentTaskService 
{
    /**
     * 查询对比实验任务
     * 
     * @param id 对比实验任务主键
     * @return 对比实验任务
     */
    public ExperimentTask selectExperimentTaskById(Long id);

    /**
     * 查询对比实验任务列表
     * 
     * @param experimentTask 对比实验任务
     * @return 对比实验任务集合
     */
    public List<ExperimentTask> selectExperimentTaskList(ExperimentTask experimentTask);

    /**
     * 新增对比实验任务
     * 
     * @param experimentTask 对比实验任务
     * @return 结果
     */
    public int insertExperimentTask(ExperimentTask experimentTask);

    /**
     * 修改对比实验任务
     * 
     * @param experimentTask 对比实验任务
     * @return 结果
     */
    public int updateExperimentTask(ExperimentTask experimentTask);

    /**
     * 批量删除对比实验任务
     * 
     * @param ids 需要删除的对比实验任务主键集合
     * @return 结果
     */
    public int deleteExperimentTaskByIds(Long[] ids);

    /**
     * 删除对比实验任务信息
     * 
     * @param id 对比实验任务主键
     * @return 结果
     */
    public int deleteExperimentTaskById(Long id);
}
