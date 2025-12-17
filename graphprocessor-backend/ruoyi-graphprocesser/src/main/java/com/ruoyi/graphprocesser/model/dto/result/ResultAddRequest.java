package com.ruoyi.graphprocesser.model.dto.result;

import lombok.Data;

import java.io.Serializable;

/**
 * dto定义的是业务处理等的数据格式
 *
 * 创建请求
 *
 */
@Data
public class ResultAddRequest implements Serializable {

    /**
     * acc、loss曲线路径
     */
    private String path;


    private static final long serialVersionUID = 1L;
}