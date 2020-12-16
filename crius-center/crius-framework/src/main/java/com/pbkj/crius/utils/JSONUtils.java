package com.pbkj.crius.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author yaoyuan
 * @createTime 2019/12/04 8:51 PM
 */
public class JSONUtils {

    /**
     * 对象转Json
     **/
    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 将JSON字符串转换为对象
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    /**
     * 将JSON字符串转换为数组对象
     */
    public static <T> List<T> parseArray(String text, Class clazz) {
        return JSON.parseArray(text, clazz);
    }
}
