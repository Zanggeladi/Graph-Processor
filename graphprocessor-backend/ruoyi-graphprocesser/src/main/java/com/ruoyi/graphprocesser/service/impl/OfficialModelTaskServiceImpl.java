package com.ruoyi.graphprocesser.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.graphprocesser.mapper.OfficialModelTaskMapper;
import com.ruoyi.graphprocesser.domain.OfficialModelTask;
import com.ruoyi.graphprocesser.service.IOfficialModelTaskService;

/**
 * 官方模型实验任务Service业务层处理
 * 
 * @author zgs
 * @date 2024-10-17
 */
@Service
public class OfficialModelTaskServiceImpl implements IOfficialModelTaskService
{
    @Autowired
    private OfficialModelTaskMapper officialModelTaskMapper;

    /**
     * 查询官方模型实验任务
     * 
     * @param id 官方模型实验任务主键
     * @return 官方模型实验任务
     */
    @Override
    public OfficialModelTask selectOfficialModelTaskById(Long id)
    {
        return officialModelTaskMapper.selectOfficialModelTaskById(id);
    }

    /**
     * 查询官方模型实验任务列表
     * 
     * @param officialModelTask 官方模型实验任务
     * @return 官方模型实验任务
     */
    @Override
    public List<OfficialModelTask> selectOfficialModelTaskList(OfficialModelTask officialModelTask)
    {
        return officialModelTaskMapper.selectOfficialModelTaskList(officialModelTask);
    }

    /**
     * 新增官方模型实验任务
     * 
     * @param officialModelTask 官方模型实验任务
     * @return 结果
     */
    @Override
    public int insertOfficialModelTask(OfficialModelTask officialModelTask)
    {
        officialModelTask.setCreateTime(DateUtils.getNowDate());
        return officialModelTaskMapper.insertOfficialModelTask(officialModelTask);
    }

    /**
     * 修改官方模型实验任务
     * 
     * @param officialModelTask 官方模型实验任务
     * @return 结果
     */
    @Override
    public int updateOfficialModelTask(OfficialModelTask officialModelTask)
    {
        officialModelTask.setUpdateTime(DateUtils.getNowDate());
        return officialModelTaskMapper.updateOfficialModelTask(officialModelTask);
    }

    /**
     * 批量删除官方模型实验任务
     * 
     * @param ids 需要删除的官方模型实验任务主键
     * @return 结果
     */
    @Override
    public int deleteOfficialModelTaskByIds(Long[] ids)
    {
        return officialModelTaskMapper.deleteOfficialModelTaskByIds(ids);
    }

    /**
     * 删除官方模型实验任务信息
     * 
     * @param id 官方模型实验任务主键
     * @return 结果
     */
    @Override
    public int deleteOfficialModelTaskById(Long id)
    {
        return officialModelTaskMapper.deleteOfficialModelTaskById(id);
    }
}
