package com.pbkj.crius.common.constant;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author yuanyang
 * @version 1.0
 * @date 2020/12/17 16:29
 */
@AllArgsConstructor
public enum SexEnum implements IEnum<Integer> {
    WOMAN(0,"女"),
    MAN(1,"男");

    private int value;
    private String desc;

    @Override
    public Integer getValue() {
        return this.value;
    }
    @JsonValue
    public String toString() {
        return this.desc;
    }
}
