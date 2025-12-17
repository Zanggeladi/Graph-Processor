package com.ruoyi.graphprocesser.model.dto.task;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 */
@Data
public class TaskAddRequest implements Serializable {
    //    private Long taskId;
    //
    //    /**
    //     * 数据集类型：官方数据集/个人数据集
    //     */
    //    private Integer datasetType;
    //
    //    /**
    //     * 数据集id
    //     */
    //    private Long datasetId;
    //
    //    /**
    //     * 创建用户id
    //     */
    //    private Long userId;


    /**
     * 任务名称
     */
    private String name;

    /**
     * 数据集类型：官方数据集/个人数据集
     */
    private Integer datasetType;

    /**
     * 数据集id
     */
    private Long datasetId;

    /**
     * 数据处理模型id
     */
    private Integer modelId;

    /**
     * 下游任务id
     */
    private Integer subtaskId;

    private String code;

    /**
     * 创建用户id
     */
    private Long userId;



    private static final long serialVersionUID = 1L;
}