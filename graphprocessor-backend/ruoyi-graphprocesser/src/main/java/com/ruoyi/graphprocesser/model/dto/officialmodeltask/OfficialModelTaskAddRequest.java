package com.ruoyi.graphprocesser.model.dto.officialmodeltask;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 */
@Data
public class OfficialModelTaskAddRequest implements Serializable {

    /**
     * 官方数据处理模型id
     */
    private Long officialModelId;

    /**
     * 数据集类型：官方数据集/个人数据集
     */
    private Integer datasetType;

    /**
     * 数据集名称
     */
    private String datasetName;

    /**
     * 数据集路径
     */
    private String datasetPath;

    /**
     * 模型参数
     */
    private String parameters;



    private static final long serialVersionUID = 1L;
}