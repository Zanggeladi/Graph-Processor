package com.ruoyi.graphprocesser.model.dto.experimenttask;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 */
@Data
public class ExperimentTaskAddRequest implements Serializable {

    /**
     * 任务名称
     */
    private String taskName;

//    /**
//     * 数据集路径
//     */
//    private String datasetPath;
    /**
     * 数据集ID
     */
    private Long datasetId;

    /**
     * 模型代码
     */
    private String modelCode;

    /**
     * 模型参数
     */
    private String parameters;

    /**
     * 对比实验主题ID
     */
    private Long themeId;

    /**
     * 创建用户ID
     */
    private Long userId;



    private static final long serialVersionUID = 1L;
}