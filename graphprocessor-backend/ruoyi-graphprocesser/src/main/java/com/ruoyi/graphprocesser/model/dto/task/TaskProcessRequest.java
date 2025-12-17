package com.ruoyi.graphprocesser.model.dto.task;

import lombok.Data;

@Data
public class TaskProcessRequest {
    /**
     * 任务名称
     */
    private String name;

    /**
     * 数据集类型
     */
    private Integer datasetType;

    /**
     * 数据集Id
     */
    private Long datasetId;

    /**
     * 模型Id
     */
    private Integer modelId;

    /**
     * 下游任务Id
     */
    private Integer subtaskId;

    private String code;

    private Long themeId;


    private static final long serialVersionUID = 1L;
}
