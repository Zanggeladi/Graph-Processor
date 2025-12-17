package com.ruoyi.graphprocesser.model.dto.dataset;

import lombok.Data;

import java.io.Serializable;

/**
 * dto定义的是业务处理等的数据格式
 *
 * 创建请求
 *
 */
@Data
public class DatasetAddRequest implements Serializable {

    /**
     * 数据集名称
     */
    private String name;

    private String path;


    private static final long serialVersionUID = 1L;
}