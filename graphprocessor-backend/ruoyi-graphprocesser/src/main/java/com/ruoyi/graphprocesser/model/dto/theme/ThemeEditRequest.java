package com.ruoyi.graphprocesser.model.dto.theme;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑请求
 *
 */
@Data
public class ThemeEditRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private String name;

    private String description;

    private static final long serialVersionUID = 1L;
}