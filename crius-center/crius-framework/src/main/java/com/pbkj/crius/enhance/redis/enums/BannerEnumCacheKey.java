package com.pbkj.crius.enhance.redis.enums;


import com.pbkj.crius.enhance.redis.CacheKey;
import com.pbkj.crius.enhance.redis.RedisWarpper;
import com.pbkj.crius.utils.SpringContextUtils;

/**
 * @author
 */
public enum BannerEnumCacheKey implements CacheKey {
    // ========= Banner轮播 ========== //
    BANNER_MODULE_TYPE(SpringContextUtils.redisProjectNamePrefix + "MODULE_TYPE", rch.getWrapper(BannerEnumCacheKey.class))
    ;
    private String profix;

    private RedisWarpper warpper;

    BannerEnumCacheKey(String profix, RedisWarpper warpper) {
        this.profix = profix;
        this.warpper = warpper;
    }

    @Override
    public RedisWarpper getWarpper() {
        return warpper;
    }

    @Override
    public String getProfix() {
        return profix;
    }
}