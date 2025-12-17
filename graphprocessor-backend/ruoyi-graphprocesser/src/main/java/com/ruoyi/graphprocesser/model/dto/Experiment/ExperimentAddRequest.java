package com.ruoyi.graphprocesser.model.dto.Experiment;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 */
@Data
public class ExperimentAddRequest implements Serializable {

    /**
     * 任务名称
     */
    private String name;


    private String code;

    private String datasetPath;

    private Long themeId;

    /**
     * 创建用户id
     */
    private Long userId;



    private static final long serialVersionUID = 1L;
}