package com.ruoyi.graphprocesser.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.graphprocesser.domain.TbExperimentResult;
import com.ruoyi.graphprocesser.mapper.ExperimentTaskMapper;
import com.ruoyi.graphprocesser.domain.ExperimentTask;
import com.ruoyi.graphprocesser.service.IExperimentTaskService;

/**
 * 对比实验任务Service业务层处理
 * 
 * @author zgs
 * @date 2024-10-27
 */
@Service
public class ExperimentTaskServiceImpl implements IExperimentTaskService 
{
    @Autowired
    private ExperimentTaskMapper experimentTaskMapper;

    /**
     * 查询对比实验任务
     * 
     * @param id 对比实验任务主键
     * @return 对比实验任务
     */
    @Override
    public ExperimentTask selectExperimentTaskById(Long id)
    {
        return experimentTaskMapper.selectExperimentTaskById(id);
    }

    /**
     * 查询对比实验任务列表
     * 
     * @param experimentTask 对比实验任务
     * @return 对比实验任务
     */
    @Override
    public List<ExperimentTask> selectExperimentTaskList(ExperimentTask experimentTask)
    {
        return experimentTaskMapper.selectExperimentTaskList(experimentTask);
    }

    /**
     * 新增对比实验任务
     * 
     * @param experimentTask 对比实验任务
     * @return 结果
     */
    @Transactional
    @Override
    public int insertExperimentTask(ExperimentTask experimentTask)
    {
        experimentTask.setCreateTime(DateUtils.getNowDate());
        int rows = experimentTaskMapper.insertExperimentTask(experimentTask);
        insertTbExperimentResult(experimentTask);
        return rows;
    }

    /**
     * 修改对比实验任务
     * 
     * @param experimentTask 对比实验任务
     * @return 结果
     */
    @Transactional
    @Override
    public int updateExperimentTask(ExperimentTask experimentTask)
    {
        experimentTask.setUpdateTime(DateUtils.getNowDate());
        experimentTaskMapper.deleteTbExperimentResultByTaskId(experimentTask.getId());
        insertTbExperimentResult(experimentTask);
        return experimentTaskMapper.updateExperimentTask(experimentTask);
    }

    /**
     * 批量删除对比实验任务
     * 
     * @param ids 需要删除的对比实验任务主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteExperimentTaskByIds(Long[] ids)
    {
        experimentTaskMapper.deleteTbExperimentResultByTaskIds(ids);
        return experimentTaskMapper.deleteExperimentTaskByIds(ids);
    }

    /**
     * 删除对比实验任务信息
     * 
     * @param id 对比实验任务主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteExperimentTaskById(Long id)
    {
        experimentTaskMapper.deleteTbExperimentResultByTaskId(id);
        return experimentTaskMapper.deleteExperimentTaskById(id);
    }

    /**
     * 新增实验结果信息
     * 
     * @param experimentTask 对比实验任务对象
     */
    public void insertTbExperimentResult(ExperimentTask experimentTask)
    {
        List<TbExperimentResult> tbExperimentResultList = experimentTask.getTbExperimentResultList();
        Long id = experimentTask.getId();
        if (StringUtils.isNotNull(tbExperimentResultList))
        {
            List<TbExperimentResult> list = new ArrayList<TbExperimentResult>();
            for (TbExperimentResult tbExperimentResult : tbExperimentResultList)
            {
                tbExperimentResult.setTaskId(id);
                list.add(tbExperimentResult);
            }
            if (list.size() > 0)
            {
                experimentTaskMapper.batchTbExperimentResult(list);
            }
        }
    }
}
