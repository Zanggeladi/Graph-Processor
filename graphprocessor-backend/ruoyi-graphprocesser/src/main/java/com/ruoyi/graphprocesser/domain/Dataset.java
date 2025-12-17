package com.ruoyi.graphprocesser.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据集对象 tb_dataset
 * 
 * @author zgs
 * @date 2024-10-26
 */
public class Dataset extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String datasetName;

    /** 描述信息 */
    @Excel(name = "描述信息")
    private String description;

    /** 标签 */
    @Excel(name = "标签")
    private String tags;

    /** 存储路径 */
    @Excel(name = "存储路径")
    private String datasetPath;

    /** 数据集大小 */
    @Excel(name = "数据集大小")
    private Long size;

    /** 版本号 */
    @Excel(name = "版本号")
    private Long version;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDatasetName(String datasetName) 
    {
        this.datasetName = datasetName;
    }

    public String getDatasetName() 
    {
        return datasetName;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }
    public void setDatasetPath(String datasetPath) 
    {
        this.datasetPath = datasetPath;
    }

    public String getDatasetPath() 
    {
        return datasetPath;
    }
    public void setSize(Long size) 
    {
        this.size = size;
    }

    public Long getSize() 
    {
        return size;
    }
    public void setVersion(Long version) 
    {
        this.version = version;
    }

    public Long getVersion() 
    {
        return version;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("datasetName", getDatasetName())
            .append("description", getDescription())
            .append("tags", getTags())
            .append("datasetPath", getDatasetPath())
            .append("size", getSize())
            .append("version", getVersion())
            .append("status", getStatus())
            .append("userId", getUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
