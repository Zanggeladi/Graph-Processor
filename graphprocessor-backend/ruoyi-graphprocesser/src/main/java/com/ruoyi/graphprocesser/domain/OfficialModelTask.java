package com.ruoyi.graphprocesser.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 官方模型实验任务对象 tb_official_model_task
 * 
 * @author zgs
 * @date 2024-10-17
 */
public class OfficialModelTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 模型id */
    @Excel(name = "模型id")
    private Long officialModelId;

    /** 数据集类型 */
    @Excel(name = "数据集类型")
    private Integer datasetType;

    /** 数据集名称 */
    @Excel(name = "数据集名称")
    private String datasetName;

    /** 个人数据集路径 */
    @Excel(name = "个人数据集路径")
    private String datasetPath;

    /** 参数 */
    @Excel(name = "参数")
    private String parameters;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private Long status;

    /** 执行信息 */
    @Excel(name = "执行信息")
    private String execMessage;

    /** 结果id */
    @Excel(name = "结果id")
    private Long resultId;

    /** 结果路径 */
    @Excel(name = "结果路径")
    private String resultPath;

    /** image路径 */
    @Excel(name = "image路径")
    private String imgPath;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOfficialModelId(Long officialModelId) 
    {
        this.officialModelId = officialModelId;
    }

    public Long getOfficialModelId() 
    {
        return officialModelId;
    }
    public void setDatasetType(Integer datasetType) 
    {
        this.datasetType = datasetType;
    }

    public Integer getDatasetType() 
    {
        return datasetType;
    }
    public void setDatasetName(String datasetName) 
    {
        this.datasetName = datasetName;
    }

    public String getDatasetName() 
    {
        return datasetName;
    }
    public void setDatasetPath(String datasetPath) 
    {
        this.datasetPath = datasetPath;
    }

    public String getDatasetPath() 
    {
        return datasetPath;
    }
    public void setParameters(String parameters) 
    {
        this.parameters = parameters;
    }

    public String getParameters() 
    {
        return parameters;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
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
    public void setResultId(Long resultId) 
    {
        this.resultId = resultId;
    }

    public Long getResultId() 
    {
        return resultId;
    }
    public void setResultPath(String resultPath) 
    {
        this.resultPath = resultPath;
    }

    public String getResultPath() 
    {
        return resultPath;
    }
    public void setImgPath(String imgPath) 
    {
        this.imgPath = imgPath;
    }

    public String getImgPath() 
    {
        return imgPath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("officialModelId", getOfficialModelId())
            .append("datasetType", getDatasetType())
            .append("datasetName", getDatasetName())
            .append("datasetPath", getDatasetPath())
            .append("parameters", getParameters())
            .append("userId", getUserId())
            .append("status", getStatus())
            .append("execMessage", getExecMessage())
            .append("resultId", getResultId())
            .append("resultPath", getResultPath())
            .append("imgPath", getImgPath())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
