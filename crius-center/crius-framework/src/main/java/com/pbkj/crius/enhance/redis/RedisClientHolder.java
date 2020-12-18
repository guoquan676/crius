package com.pbkj.crius.enhance.redis;


import com.pbkj.crius.utils.YamlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ GZQ yh
 */
@Slf4j
@Component
public class RedisClientHolder {
    private final static ConcurrentHashMap<Class<? extends CacheKey>, RedisWarpper> redisHolder = new ConcurrentHashMap<>();

    private static String redisUrl = "r-2ze8xsc4p3fp8o2zxw.redis.rds.aliyuncs.com";
    private static Integer redisPort = 6379;
    private static String redisPassword = "123456";
    /**
     * 数据库索引，默认0库
     */
    private static int idx = 0;
    private static int rdCount = 0;

//    static {
//        try {
//            Map<String, Object> stringObjectMap = YamlUtils.yamlHandler();
//            String serverAddr = (String) stringObjectMap.get("spring.cloud.nacos.config.server-addr");
//            String namespace = (String) stringObjectMap.get("spring.cloud.nacos.config.namespace");
//            String group = (String) stringObjectMap.get("spring.cloud.nacos.config.group");
//            String name = (String) stringObjectMap.get("spring.cloud.nacos.config.name");
//            Properties pro = new Properties();
//            pro.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
//            pro.put(PropertyKeyConst.NAMESPACE, namespace);
//            ConfigService configService = NacosFactory.createConfigService(pro);
//            String config = configService.getConfig(name, group, 5000);
//            Map<String, Object> configMap = YamlUtils.parseYaml2Map(config);
//            configService.addListener(name, group, new ConfingListener());
//            HashMap<String, Object> redis1 = (HashMap<String, Object>) configMap.get("redis");
//            redisUrl = (String) redis1.get("url");
//            redisPort = (int) redis1.get("port");
//            redisPassword = String.valueOf(redis1.get("password"));
//            idx = (int)redis1.get("rdIdx");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    static {
        try {
            Map<String, Object> stringObjectMap = YamlUtils.yamlHandler();
            String url = (String) stringObjectMap.get("redis.url");
            String port = String.valueOf(stringObjectMap.get("redis.port")) ;
            String password = String.valueOf(stringObjectMap.get("redis.password"));
            String rdIdx = String.valueOf(stringObjectMap.get("redis.rdIdx"));
            redisUrl = url;
            redisPort = Integer.parseInt(port);
            redisPassword = password;
            idx = Integer.parseInt(rdIdx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RedisClientHolder() {
        rdCount = rdCount + 1;
        System.out.println(RedisClientHolder.redisUrl);
        System.out.println(" RedisClientHolder rd 加载:" + rdCount);
    }

    public RedisClientHolder(String host, int port, String pwd) {
        log.info("初始化redisClient init：");
        redisUrl = host;
        redisPort = port;
        redisPassword = pwd;
    }

    public RedisClientHolder(String host, int port, String pwd, int rdIdx) {
        log.info("初始化redisClient init：");
        redisUrl = host;
        redisPort = port;
        redisPassword = pwd;
        idx = rdIdx;
    }

    public RedisWarpper getWrapper(Class<? extends CacheKey> cacheKey) {
        return redisHolder.computeIfAbsent(cacheKey, RedisClientHolder::createWarpper);
    }

    /**
     * 修改这里如果增加了redis实例的化
     *
     * @param cacheKey
     * @return
     */
    private static RedisWarpper createWarpper(Class<? extends CacheKey> cacheKey) {
        log.info(redisPort + "==redisUrl:" + redisUrl + "redisPassword:" + redisPassword);
        return new RedisWarpper(createPool(redisUrl, redisPort, redisPassword));
    }

    private static JedisPool createPool(String serverIp, int port, String password) {
        rdCount = rdCount + 1;
        System.out.println("JedisPool rd 加载:" + rdCount);
        //redis
        JedisPool pool;
        JedisPoolConfig config = new JedisPoolConfig();
        // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(-1);
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(2000);
        // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(1000 * 1);
        //向资源池借用连接时是否做连接有效性检查(ping)，无效连接会被移除false
        config.setMinIdle(50);
        config.setTestOnBorrow(false);
        if (password == null || "".equals(password)) {
            pool = new JedisPool(config, serverIp, port, 1000);
        } else {
            pool = new JedisPool(config, serverIp, port, 1000, password, idx);
        }
        return pool;
    }

}