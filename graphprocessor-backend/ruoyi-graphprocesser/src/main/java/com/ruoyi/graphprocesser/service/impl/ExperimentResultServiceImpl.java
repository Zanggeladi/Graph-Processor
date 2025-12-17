package com.ruoyi.graphprocesser.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.graphprocesser.mapper.ExperimentResultMapper;
import com.ruoyi.graphprocesser.domain.ExperimentResult;
import com.ruoyi.graphprocesser.service.IExperimentResultService;

/**
 * 实验结果Service业务层处理
 * 
 * @author zgs
 * @date 2024-09-02
 */
@Service
public class ExperimentResultServiceImpl implements IExperimentResultService
{
    @Autowired
    private ExperimentResultMapper experimentResultMapper;

    /**
     * 查询实验结果
     * 
     * @param id 实验结果主键
     * @return 实验结果
     */
    @Override
    public ExperimentResult selectExperimentResultById(Long id)
    {
        return experimentResultMapper.selectExperimentResultById(id);
    }

    /**
     * 查询实验结果列表
     * 
     * @param experimentResult 实验结果
     * @return 实验结果
     */
    @Override
    public List<ExperimentResult> selectExperimentResultList(ExperimentResult experimentResult)
    {
        return experimentResultMapper.selectExperimentResultList(experimentResult);
    }

    /**
     * 新增实验结果
     * 
     * @param experimentResult 实验结果
     * @return 结果
     */
    @Override
    public int insertExperimentResult(ExperimentResult experimentResult)
    {
        experimentResult.setCreateTime(DateUtils.getNowDate());
        return experimentResultMapper.insertExperimentResult(experimentResult);
    }

    /**
     * 修改实验结果
     * 
     * @param experimentResult 实验结果
     * @return 结果
     */
    @Override
    public int updateExperimentResult(ExperimentResult experimentResult)
    {
        experimentResult.setUpdateTime(DateUtils.getNowDate());
        return experimentResultMapper.updateExperimentResult(experimentResult);
    }

    /**
     * 批量删除实验结果
     * 
     * @param ids 需要删除的实验结果主键
     * @return 结果
     */
    @Override
    public int deleteExperimentResultByIds(Long[] ids)
    {
        return experimentResultMapper.deleteExperimentResultByIds(ids);
    }

    /**
     * 删除实验结果信息
     * 
     * @param id 实验结果主键
     * @return 结果
     */
    @Override
    public int deleteExperimentResultById(Long id)
    {
        return experimentResultMapper.deleteExperimentResultById(id);
    }
}
