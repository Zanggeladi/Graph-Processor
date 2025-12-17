package com.ruoyi.graphprocesser.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 官方模型图数据处理任务结果对象 tb_official_model_task_result
 * 
 * @author zgs
 * @date 2024-11-20
 */
public class OfficialModelTaskResult extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 任务id */
    @Excel(name = "任务id")
    private Long taskId;

    /** 运行输出 */
    @Excel(name = "运行输出")
    private String outputMessage;

    /** 结果文件 */
    @Excel(name = "结果文件")
    private String resultFilePath;

    /** 训练曲线 */
    @Excel(name = "训练曲线")
    private String curvePath;

    /** 节点嵌入可视化 */
    @Excel(name = "节点嵌入可视化")
    private String tsnePath;

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
    public void setOutputMessage(String outputMessage) 
    {
        this.outputMessage = outputMessage;
    }

    public String getOutputMessage() 
    {
        return outputMessage;
    }
    public void setResultFilePath(String resultFilePath) 
    {
        this.resultFilePath = resultFilePath;
    }

    public String getResultFilePath() 
    {
        return resultFilePath;
    }
    public void setCurvePath(String curvePath) 
    {
        this.curvePath = curvePath;
    }

    public String getCurvePath() 
    {
        return curvePath;
    }
    public void setTsnePath(String tsnePath) 
    {
        this.tsnePath = tsnePath;
    }

    public String getTsnePath() 
    {
        return tsnePath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("outputMessage", getOutputMessage())
            .append("resultFilePath", getResultFilePath())
            .append("curvePath", getCurvePath())
            .append("tsnePath", getTsnePath())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
