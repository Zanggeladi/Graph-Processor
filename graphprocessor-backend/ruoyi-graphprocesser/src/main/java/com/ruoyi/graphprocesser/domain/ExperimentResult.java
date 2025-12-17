package com.ruoyi.graphprocesser.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 实验结果对象 tb_experiment_result
 * 
 * @author zgs
 * @date 2024-09-02
 */
public class ExperimentResult extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 实验任务id */
    @Excel(name = "实验任务id")
    private Long taskId;

    /** 实验指标 */
    @Excel(name = "实验指标")
    private String metrics;

    /** 结果文件路径 */
    @Excel(name = "结果文件路径")
    private String resultFilePath;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
    }
    public void setMetrics(String metrics) 
    {
        this.metrics = metrics;
    }

    public String getMetrics() 
    {
        return metrics;
    }
    public void setResultFilePath(String resultFilePath) 
    {
        this.resultFilePath = resultFilePath;
    }

    public String getResultFilePath() 
    {
        return resultFilePath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("metrics", getMetrics())
            .append("resultFilePath", getResultFilePath())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
