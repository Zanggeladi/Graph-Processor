package com.ruoyi.graphprocesser.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对比实验任务对象 tb_experiment_task
 * 
 * @author zgs
 * @date 2024-10-27
 */
public class ExperimentTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 任务描述 */
    @Excel(name = "任务描述")
    private String description;

    /** 主题id */
    @Excel(name = "主题id")
    private Long themeId;

    /** 数据集id */
    @Excel(name = "数据集id")
    private Long datasetId;

    /** 模型代码 */
    @Excel(name = "模型代码")
    private String modeCode;

    /** 实验超参数 */
    @Excel(name = "实验超参数")
    private String hyperparameters;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private Long status;

    /** 执行信息 */
    @Excel(name = "执行信息")
    private String execMessage;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 实验结果信息 */
    private List<TbExperimentResult> tbExperimentResultList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTaskName(String taskName) 
    {
        this.taskName = taskName;
    }

    public String getTaskName() 
    {
        return taskName;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setThemeId(Long themeId) 
    {
        this.themeId = themeId;
    }

    public Long getThemeId() 
    {
        return themeId;
    }
    public void setDatasetId(Long datasetId) 
    {
        this.datasetId = datasetId;
    }

    public Long getDatasetId() 
    {
        return datasetId;
    }
    public void setModeCode(String modeCode) 
    {
        this.modeCode = modeCode;
    }

    public String getModeCode() 
    {
        return modeCode;
    }
    public void setHyperparameters(String hyperparameters) 
    {
        this.hyperparameters = hyperparameters;
    }

    public String getHyperparameters() 
    {
        return hyperparameters;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setExecMessage(String execMessage) 
    {
        this.execMessage = execMessage;
    }

    public String getExecMessage() 
    {
        return execMessage;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public List<TbExperimentResult> getTbExperimentResultList()
    {
        return tbExperimentResultList;
    }

    public void setTbExperimentResultList(List<TbExperimentResult> tbExperimentResultList)
    {
        this.tbExperimentResultList = tbExperimentResultList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskName", getTaskName())
            .append("description", getDescription())
            .append("themeId", getThemeId())
            .append("datasetId", getDatasetId())
            .append("modeCode", getModeCode())
            .append("hyperparameters", getHyperparameters())
            .append("status", getStatus())
            .append("execMessage", getExecMessage())
            .append("userId", getUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("tbExperimentResultList", getTbExperimentResultList())
            .toString();
    }
}
