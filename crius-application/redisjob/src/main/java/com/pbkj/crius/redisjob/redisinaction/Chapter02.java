package com.pbkj.crius.redisjob.redisinaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gzq
 * 用redis实现文章排行网站
 */
public class Chapter02 {

    //tips1  文章分组排名
    //tips2  自动缓存功能


    public static void main(String[] args) {
        long min = Math.min(1000L - 10L, 100L);
        System.out.println(min);
    }
}
