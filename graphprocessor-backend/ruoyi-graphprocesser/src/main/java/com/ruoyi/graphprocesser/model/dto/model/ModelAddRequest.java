package com.ruoyi.graphprocesser.model.dto.model;

import lombok.Data;

import java.io.Serializable;

/**
 * dto定义的是业务处理等的数据格式
 *
 * 创建请求
 *
 */
@Data
public class ModelAddRequest implements Serializable {

    /**
     * 模型名称
     */
    private String name;

    private String code;


    private static final long serialVersionUID = 1L;
}