package com.ruoyi.graphprocesser.model.dto.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑请求
 *
 */
@Data
public class ModelEditRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private String name;

    private String code;

    private static final long serialVersionUID = 1L;
}