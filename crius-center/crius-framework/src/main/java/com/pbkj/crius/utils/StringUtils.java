package com.pbkj.crius.utils;

import java.util.regex.Pattern;

/**
 * StringUtils工具类
 *
 * @author Peng
 */
public class StringUtils {
    private static final Pattern PATTERN = Pattern.compile("<[^<|^>]*>");

    /**
     * 字符串空值判断
     * jedis 为空回返回 String = "null"~~~~~ 这个方法专门用于检查的
     * @param str 字符串
     * @return true 空 false 非空
     */
    public static boolean isJedisEmpty(String str) {
        return null == str || str.trim().length() == 0 || "null".equalsIgnoreCase(str.trim()) || "(null)".equalsIgnoreCase(str.trim());
    }

    public static boolean isJedisNotEmpty(String str) {
        return !isJedisEmpty(str);
    }




}