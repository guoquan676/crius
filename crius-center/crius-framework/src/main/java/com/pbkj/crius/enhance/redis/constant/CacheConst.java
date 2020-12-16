package com.pbkj.crius.enhance.redis.constant;

/**
 * @author yaoyuan
 * @createTime 2019/12/10 10:47 AM
 */
public class CacheConst {
    /**
     * 失效时间
     */
    public static final int EXPIRE_TIME = 6 * 60 * 60;
    public static final int ONE_HOUR_TIME = 60 * 60;
    public static final int FIVE_MINUTE_TIME = 5 * 60;
    public static final int TEN_MINUTE_TIME = 10 * 60;
    public static final int FIFTEEN_MINUTE_TIME = 15 * 60;
    public static final int ONE_DAY_TIME = 24 * 60 * 60;
    public static final int THREE_DAY_TIME = 3 * 24 * 60 * 60;
    public static final int SEVEN_DAY_TIME = 7 * 24 * 60 * 60;
    public static final int THIRTY_DAY_TIME = 30 * 24 * 60 * 60;
    public static final int YEAR_DAY_TIME = 12 * 30 * 24 * 60 * 60;
    private static final String SIGN = "XIAOKU-";
    private static final String VERSION = "v3";

    /**
     * threshold 统一前缀
     */
    public static final String BASE_THRESHOLD_PREFIX = "threshold-";

    /**
     * redis const
     */

    public static final String MAX = "+inf";
    public static final String MIN = "-inf";
}
