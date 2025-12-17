package com.ruoyi.graphprocesser.model.dto.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑请求
 *
 */
@Data
public class ResultEditRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private String path;

    private static final long serialVersionUID = 1L;
}