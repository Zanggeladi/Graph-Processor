package com.ruoyi.graphprocesser.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 模型对象 tb_model
 * 
 * @author zgs
 * @date 2024-10-26
 */
public class Model extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String modelName;

    /** 标签 */
    @Excel(name = "标签")
    private String tags;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 代码 */
    @Excel(name = "代码")
    private String code;

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
    public void setModelName(String modelName) 
    {
        this.modelName = modelName;
    }

    public String getModelName() 
    {
        return modelName;
    }
    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
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
            .append("modelName", getModelName())
            .append("tags", getTags())
            .append("description", getDescription())
            .append("code", getCode())
            .append("userId", getUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
