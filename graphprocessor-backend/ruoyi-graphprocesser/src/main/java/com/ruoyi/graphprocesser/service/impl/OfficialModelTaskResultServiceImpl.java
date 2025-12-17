package com.ruoyi.graphprocesser.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.graphprocesser.mapper.OfficialModelTaskResultMapper;
import com.ruoyi.graphprocesser.domain.OfficialModelTaskResult;
import com.ruoyi.graphprocesser.service.IOfficialModelTaskResultService;

/**
 * 官方模型图数据处理任务结果Service业务层处理
 * 
 * @author zgs
 * @date 2024-11-20
 */
@Service
public class OfficialModelTaskResultServiceImpl implements IOfficialModelTaskResultService 
{
    @Autowired
    private OfficialModelTaskResultMapper officialModelTaskResultMapper;

    @Override
    public OfficialModelTaskResult selectOfficialModelTaskResultByTaskId(Long taskId)
    {
        return officialModelTaskResultMapper.selectOfficialModelTaskResultByTaskId(taskId);
    }

    /**
     * 查询官方模型图数据处理任务结果
     * 
     * @param id 官方模型图数据处理任务结果主键
     * @return 官方模型图数据处理任务结果
     */
    @Override
    public OfficialModelTaskResult selectOfficialModelTaskResultById(Long id)
    {
        return officialModelTaskResultMapper.selectOfficialModelTaskResultById(id);
    }

    /**
     * 查询官方模型图数据处理任务结果列表
     * 
     * @param officialModelTaskResult 官方模型图数据处理任务结果
     * @return 官方模型图数据处理任务结果
     */
    @Override
    public List<OfficialModelTaskResult> selectOfficialModelTaskResultList(OfficialModelTaskResult officialModelTaskResult)
    {
        return officialModelTaskResultMapper.selectOfficialModelTaskResultList(officialModelTaskResult);
    }

    /**
     * 新增官方模型图数据处理任务结果
     * 
     * @param officialModelTaskResult 官方模型图数据处理任务结果
     * @return 结果
     */
    @Override
    public int insertOfficialModelTaskResult(OfficialModelTaskResult officialModelTaskResult)
    {
        officialModelTaskResult.setCreateTime(DateUtils.getNowDate());
        return officialModelTaskResultMapper.insertOfficialModelTaskResult(officialModelTaskResult);
    }

    /**
     * 修改官方模型图数据处理任务结果
     * 
     * @param officialModelTaskResult 官方模型图数据处理任务结果
     * @return 结果
     */
    @Override
    public int updateOfficialModelTaskResult(OfficialModelTaskResult officialModelTaskResult)
    {
        officialModelTaskResult.setUpdateTime(DateUtils.getNowDate());
        return officialModelTaskResultMapper.updateOfficialModelTaskResult(officialModelTaskResult);
    }

    /**
     * 批量删除官方模型图数据处理任务结果
     * 
     * @param ids 需要删除的官方模型图数据处理任务结果主键
     * @return 结果
     */
    @Override
    public int deleteOfficialModelTaskResultByIds(Long[] ids)
    {
        return officialModelTaskResultMapper.deleteOfficialModelTaskResultByIds(ids);
    }

    /**
     * 删除官方模型图数据处理任务结果信息
     * 
     * @param id 官方模型图数据处理任务结果主键
     * @return 结果
     */
    @Override
    public int deleteOfficialModelTaskResultById(Long id)
    {
        return officialModelTaskResultMapper.deleteOfficialModelTaskResultById(id);
    }
}
