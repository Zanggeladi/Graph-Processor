package com.ruoyi.graphprocesser.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 官方模型对象 tb_official_model
 * 
 * @author zgs
 * @date 2024-11-10
 */
public class OfficialModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 类型 */
    @Excel(name = "类型")
    private Integer type;

    /** 标签 */
    @Excel(name = "标签")
    private String tags;

    /** 下游任务 */
    @Excel(name = "下游任务")
    private String subtask;

    /** 简述 */
    @Excel(name = "简述")
    private String shortDescription;

    /** 详述 */
    @Excel(name = "详述")
    private String detailedDescription;

    /** 模型图片 */
    @Excel(name = "模型图片")
    private String modelImgPath;

    /** 模型代码 */
    @Excel(name = "模型代码")
    private String codePath;

    /** 参数列表 */
    @Excel(name = "参数列表")
    private String parameters;

    /** 可选数据集列表（多个数据集用逗号分隔） */
    @Excel(name = "可选数据集列表", readConverterExp = "多=个数据集用逗号分隔")
    private String datasets;

    /** 个人数据集文件格式 */
    @Excel(name = "个人数据集文件格式")
    private String requiredFiles;

    /** 数据集文件简述 */
    @Excel(name = "数据集文件简述")
    private String datasetsDescription;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }
    public void setSubtask(String subtask) 
    {
        this.subtask = subtask;
    }

    public String getSubtask() 
    {
        return subtask;
    }
    public void setShortDescription(String shortDescription) 
    {
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() 
    {
        return shortDescription;
    }
    public void setDetailedDescription(String detailedDescription) 
    {
        this.detailedDescription = detailedDescription;
    }

    public String getDetailedDescription() 
    {
        return detailedDescription;
    }
    public void setModelImgPath(String modelImgPath) 
    {
        this.modelImgPath = modelImgPath;
    }

    public String getModelImgPath() 
    {
        return modelImgPath;
    }
    public void setCodePath(String codePath) 
    {
        this.codePath = codePath;
    }

    public String getCodePath() 
    {
        return codePath;
    }
    public void setParameters(String parameters) 
    {
        this.parameters = parameters;
    }

    public String getParameters() 
    {
        return parameters;
    }
    public void setDatasets(String datasets) 
    {
        this.datasets = datasets;
    }

    public String getDatasets() 
    {
        return datasets;
    }
    public void setRequiredFiles(String requiredFiles) 
    {
        this.requiredFiles = requiredFiles;
    }

    public String getRequiredFiles() 
    {
        return requiredFiles;
    }
    public void setDatasetsDescription(String datasetsDescription) 
    {
        this.datasetsDescription = datasetsDescription;
    }

    public String getDatasetsDescription() 
    {
        return datasetsDescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("tags", getTags())
            .append("subtask", getSubtask())
            .append("shortDescription", getShortDescription())
            .append("detailedDescription", getDetailedDescription())
            .append("modelImgPath", getModelImgPath())
            .append("codePath", getCodePath())
            .append("parameters", getParameters())
            .append("datasets", getDatasets())
            .append("requiredFiles", getRequiredFiles())
            .append("datasetsDescription", getDatasetsDescription())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
