package com.ruoyi.graphprocesser.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务处理状态枚举
 *
 */
@Getter
public enum TaskStatusEnum {

    PENDING("pending", "0"),
    RUNNING("running", "1"),
    COMPLETED("completed", "2"),
    FAILED("failed", "3");


    private final String text;

    private final String value;

    TaskStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static TaskStatusEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (TaskStatusEnum anEnum : TaskStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
