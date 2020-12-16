package com.pbkj.crius.enhance.redis;

import java.util.Collection;

/**
 * @author yaoyuan
 * @createTime 2019 4 26 11:30 AM
 */

public class RedisSetListModel<T> {

    private String key;
    private Collection<T> value;
    private int seconds = -1; // -1 表示不过期

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Collection<T> getValue() {
        return value;
    }

    public void setValue(Collection<T> value) {
        this.value = value;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
