package com.ruoyi.graphprocesser.model.dto.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class ResultUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private String path;


    private static final long serialVersionUID = 1L;
}