package com.pbkj.crius.enhance.redis;

/**
 * @author yaoyuan
 * @createTime 2019 5 07 9:32 AM
 */
public interface CacheKey {

    RedisClientHolder rch = new RedisClientHolder();
    RedisWarpper getWarpper();
    String getProfix();
}