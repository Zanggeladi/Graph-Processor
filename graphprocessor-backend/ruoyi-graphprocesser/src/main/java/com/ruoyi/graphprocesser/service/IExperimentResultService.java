package com.ruoyi.graphprocesser.service;

import java.util.List;

import com.ruoyi.graphprocesser.domain.ExperimentResult;

/**
 * 实验结果Service接口
 * 
 * @author zgs
 * @date 2024-09-02
 */
public interface IExperimentResultService
{
    /**
     * 查询实验结果
     * 
     * @param id 实验结果主键
     * @return 实验结果
     */
    public ExperimentResult selectExperimentResultById(Long id);

    /**
     * 查询实验结果列表
     * 
     * @param experimentResult 实验结果
     * @return 实验结果集合
     */
    public List<ExperimentResult> selectExperimentResultList(ExperimentResult experimentResult);

    /**
     * 新增实验结果
     * 
     * @param experimentResult 实验结果
     * @return 结果
     */
    public int insertExperimentResult(ExperimentResult experimentResult);

    /**
     * 修改实验结果
     * 
     * @param experimentResult 实验结果
     * @return 结果
     */
    public int updateExperimentResult(ExperimentResult experimentResult);

    /**
     * 批量删除实验结果
     * 
     * @param ids 需要删除的实验结果主键集合
     * @return 结果
     */
    public int deleteExperimentResultByIds(Long[] ids);

    /**
     * 删除实验结果信息
     * 
     * @param id 实验结果主键
     * @return 结果
     */
    public int deleteExperimentResultById(Long id);
}
