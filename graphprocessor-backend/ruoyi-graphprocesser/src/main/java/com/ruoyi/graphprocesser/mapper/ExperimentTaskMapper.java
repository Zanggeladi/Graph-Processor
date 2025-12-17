package com.ruoyi.graphprocesser.mapper;

import java.util.List;
import com.ruoyi.graphprocesser.domain.ExperimentTask;
import com.ruoyi.graphprocesser.domain.TbExperimentResult;

/**
 * 对比实验任务Mapper接口
 * 
 * @author zgs
 * @date 2024-10-27
 */
public interface ExperimentTaskMapper 
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
     * 删除对比实验任务
     * 
     * @param id 对比实验任务主键
     * @return 结果
     */
    public int deleteExperimentTaskById(Long id);

    /**
     * 批量删除对比实验任务
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExperimentTaskByIds(Long[] ids);

    /**
     * 批量删除实验结果
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbExperimentResultByTaskIds(Long[] ids);
    
    /**
     * 批量新增实验结果
     * 
     * @param tbExperimentResultList 实验结果列表
     * @return 结果
     */
    public int batchTbExperimentResult(List<TbExperimentResult> tbExperimentResultList);
    

    /**
     * 通过对比实验任务主键删除实验结果信息
     * 
     * @param id 对比实验任务ID
     * @return 结果
     */
    public int deleteTbExperimentResultByTaskId(Long id);
}
