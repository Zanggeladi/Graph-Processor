package com.ruoyi.graphprocesser.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对比实验主题对象 tb_experiment_theme
 * 
 * @author zgs
 * @date 2024-10-26
 */
public class ExperimentTheme extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 对比实验主题名称 */
    @Excel(name = "对比实验主题名称")
    private String themeName;

    /** 对比实验主题描述 */
    @Excel(name = "对比实验主题描述")
    private String description;

    /** 关联的用户id，引用sys_user表(id) */
    @Excel(name = "关联的用户id，引用sys_user表(id)")
    private Long userId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setThemeName(String themeName) 
    {
        this.themeName = themeName;
    }

    public String getThemeName() 
    {
        return themeName;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
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
            .append("themeName", getThemeName())
            .append("description", getDescription())
            .append("userId", getUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
