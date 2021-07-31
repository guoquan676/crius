package com.gzq.crius.bloomfilter;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.StringJoiner;

/**
 * @description: 服务类
 * @author guozhenquan
 * @date 2021/7/31 15:47
 * @version 1.0
 */
@Slf4j
public class BloomFilterService implements InitializingBean {
    private static final int SIXTEEN_INT = 16;
    private static final String UNDERLINE = "_";
    private static final String BLOOM_SERVICE_PREFIX = "BLOOM";

    private final BloomFilterConfig.BloomConfigItem config;

    private RBloomFilter<Object> bloomFilter;

    @Autowired
    RedissonClient redissonClient;

    public BloomFilterService(BloomFilterConfig.BloomConfigItem config) {
        this.config = config;
    }


    /**
     * bean属性初始化⽅法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Boolean aBoolean = this.tryInit(this.config.getExpectedInsertions(), this.config.getFalseProbability());
        if(Boolean.FALSE.equals(aBoolean)){
            throw new RuntimeException("init bloom filter failed ");
        }
    }

    /**
     * 添加元素
     */
    public boolean add(Object obj) {
        return bloomFilter.add(obj);
    }

    /**
     * 是否包含元素
     */
    public boolean contain(Object obj) {
        return bloomFilter.contains(obj);
    }

    public Boolean tryInit(long expectedInsertions, double falseProbability) {
        String bloomKey = getBloomKey();
        this.bloomFilter = redissonClient.getBloomFilter(bloomKey);
        log.info("--->初始化BloomFilter:[{}]", this.config.getName());

        if (this.bloomFilter.isExists()) {
            if (this.bloomFilter.getExpectedInsertions() != expectedInsertions || this.bloomFilter.getFalseProbability() != falseProbability) {
                // 如果Redis中已存在布隆过滤器 且参数与配置不⼀样 则抛出异常
                throw new RuntimeException("init bloom filter failed , config param does not match origin");
            }
        } else { // Redis中不存在布隆过滤器 则创建
            try {
                return this.bloomFilter.tryInit(expectedInsertions, falseProbability);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("init bloom filter failed , some exception happened in redisson");
            }
        }
        return false;
    }

    /**
     * ⽣成布隆过滤器的 Redis key
     */
    private String getBloomKey() {
        // key防冲突处理
        // prefix + key + hash
        String key = this.config.getKey();
        int hash = key.hashCode();
        hash = hash ^ (hash >>> SIXTEEN_INT);
        return new StringJoiner(UNDERLINE).add(BLOOM_SERVICE_PREFIX).add(key).add(Integer.toString(hash)).toString();
    }
}
