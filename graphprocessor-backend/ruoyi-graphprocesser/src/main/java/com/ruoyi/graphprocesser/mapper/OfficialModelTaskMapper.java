package com.ruoyi.graphprocesser.mapper;

import java.util.List;

import com.ruoyi.graphprocesser.domain.OfficialModelTask;

/**
 * 官方模型实验任务Mapper接口
 * 
 * @author zgs
 * @date 2024-10-17
 */
public interface OfficialModelTaskMapper
{
    /**
     * 查询官方模型实验任务
     * 
     * @param id 官方模型实验任务主键
     * @return 官方模型实验任务
     */
    public OfficialModelTask selectOfficialModelTaskById(Long id);

    /**
     * 查询官方模型实验任务列表
     * 
     * @param officialModelTask 官方模型实验任务
     * @return 官方模型实验任务集合
     */
    public List<OfficialModelTask> selectOfficialModelTaskList(OfficialModelTask officialModelTask);

    /**
     * 新增官方模型实验任务
     * 
     * @param officialModelTask 官方模型实验任务
     * @return 结果
     */
    public int insertOfficialModelTask(OfficialModelTask officialModelTask);

    /**
     * 修改官方模型实验任务
     * 
     * @param officialModelTask 官方模型实验任务
     * @return 结果
     */
    public int updateOfficialModelTask(OfficialModelTask officialModelTask);

    /**
     * 删除官方模型实验任务
     * 
     * @param id 官方模型实验任务主键
     * @return 结果
     */
    public int deleteOfficialModelTaskById(Long id);

    /**
     * 批量删除官方模型实验任务
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOfficialModelTaskByIds(Long[] ids);
}
