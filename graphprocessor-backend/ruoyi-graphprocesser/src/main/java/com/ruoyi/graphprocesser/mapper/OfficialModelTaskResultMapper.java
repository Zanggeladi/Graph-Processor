package com.ruoyi.graphprocesser.mapper;

import java.util.List;
import com.ruoyi.graphprocesser.domain.OfficialModelTaskResult;

/**
 * 官方模型图数据处理任务结果Mapper接口
 * 
 * @author zgs
 * @date 2024-11-20
 */
public interface OfficialModelTaskResultMapper 
{
    public OfficialModelTaskResult selectOfficialModelTaskResultByTaskId(Long taskId);

    /**
     * 查询官方模型图数据处理任务结果
     * 
     * @param id 官方模型图数据处理任务结果主键
     * @return 官方模型图数据处理任务结果
     */
    public OfficialModelTaskResult selectOfficialModelTaskResultById(Long id);

    /**
     * 查询官方模型图数据处理任务结果列表
     * 
     * @param officialModelTaskResult 官方模型图数据处理任务结果
     * @return 官方模型图数据处理任务结果集合
     */
    public List<OfficialModelTaskResult> selectOfficialModelTaskResultList(OfficialModelTaskResult officialModelTaskResult);

    /**
     * 新增官方模型图数据处理任务结果
     * 
     * @param officialModelTaskResult 官方模型图数据处理任务结果
     * @return 结果
     */
    public int insertOfficialModelTaskResult(OfficialModelTaskResult officialModelTaskResult);

    /**
     * 修改官方模型图数据处理任务结果
     * 
     * @param officialModelTaskResult 官方模型图数据处理任务结果
     * @return 结果
     */
    public int updateOfficialModelTaskResult(OfficialModelTaskResult officialModelTaskResult);

    /**
     * 删除官方模型图数据处理任务结果
     * 
     * @param id 官方模型图数据处理任务结果主键
     * @return 结果
     */
    public int deleteOfficialModelTaskResultById(Long id);

    /**
     * 批量删除官方模型图数据处理任务结果
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOfficialModelTaskResultByIds(Long[] ids);
}
