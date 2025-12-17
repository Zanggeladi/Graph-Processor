package com.ruoyi.graphprocesser.model.dto.Experiment;

import lombok.Data;

@Data
public class ExperimentProcessRequest {
    /**
     * 任务名称
     */
    private String name;

    private String code;

    private String datasetPath;

    private Long themeId;



    private static final long serialVersionUID = 1L;
}
