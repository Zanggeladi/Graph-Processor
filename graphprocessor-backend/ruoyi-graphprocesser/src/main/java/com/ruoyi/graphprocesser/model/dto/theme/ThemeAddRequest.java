package com.ruoyi.graphprocesser.model.dto.theme;

import lombok.Data;

import java.io.Serializable;

/**
 * dto定义的是业务处理等的数据格式
 *
 * 创建请求
 *
 */
@Data
public class ThemeAddRequest implements Serializable {

    /**
     * 主题名称
     */
    private String name;

    private String description;


    private static final long serialVersionUID = 1L;
}