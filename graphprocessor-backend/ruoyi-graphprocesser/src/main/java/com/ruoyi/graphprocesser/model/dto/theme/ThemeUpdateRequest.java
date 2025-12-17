package com.ruoyi.graphprocesser.model.dto.theme;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class ThemeUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private String name;

    private String description;


    private static final long serialVersionUID = 1L;
}