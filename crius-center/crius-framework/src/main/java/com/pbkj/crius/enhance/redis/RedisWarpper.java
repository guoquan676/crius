package com.pbkj.crius.enhance.redis;

import com.pbkj.crius.common.utils.SafeRun;
import com.pbkj.crius.enhance.redis.constant.CacheConst;
import com.pbkj.crius.utils.JSONUtils;
import com.pbkj.crius.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;


/**
 * Redis工具类
 *
 * @author Peng
 */
public class RedisWarpper {
    private static Logger logger = LoggerFactory.getLogger(RedisWarpper.class);

    private JedisPool pool = null;

    public RedisWarpper(JedisPool pool) {
        this.pool = pool;
    }


    /**
     * <p>删除指定的key,也可以传入一个包含key的数组</p>
     *
     * @param keys 一个key  也可以使 string 数组
     * @return 返回删除成功的个数
     */
    public Long remove(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(keys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public <R, T> Map<R, List<T>> getListByPipeline(Collection<R> sourceKeys, Function<R, String> keyFunction, Function<T, R> extractor, Class<T> format) {
        Jedis jedis = null;
        Map<R, List<T>> result = new HashMap<>();
        List<R> sourceKeysList = new ArrayList<>(sourceKeys);
        try {
            List<String> realKey = sourceKeys.stream().distinct().map(keyFunction).collect(Collectors.toList());
            jedis = pool.getResource();
            Pipeline pipelined = jedis.pipelined();
            realKey.forEach(key -> pipelined.get(key));
            List<Object> objects = pipelined.syncAndReturnAll();
            for (int i = 0; i < objects.size(); i++) {
                Object o = objects.get(i);
                R sourceKey = sourceKeysList.get(i);
                String o1 = (String) o;
                if (StringUtils.isJedisEmpty(o1)) {
                    continue;
                }
                List<T> ts = JSONUtils.parseArray((String) o, format);
                if (CollectionUtils.isNotEmpty(ts)) {
                    R key = extractor.apply(ts.get(0));
                    result.put(key, ts);
                } else { // 缓存存的是空list []
                    result.put(sourceKey, ts);
                }
            }
        } catch (Exception e) {
            logger.error("getListByPipeline error,", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 默认使用json反序列化
     *
     * @return
     */
    public <R, T> Map<R, T> getByPipeline(Collection<String> keys, Class<T> format, Function<T, R> extractor) {
        Jedis jedis = null;
        Map<R, T> result = new HashMap<>();
        try {
            jedis = pool.getResource();
            Pipeline pipelined = jedis.pipelined();
            keys.stream().forEach(key -> pipelined.get(key));
            List<Object> objects = pipelined.syncAndReturnAll();
            result = objects.stream()
                    .filter(o -> StringUtils.isJedisNotEmpty((String) o))
                    .map(o -> JSONUtils.parseObject((String) o, format))
                    .distinct()
                    .collect(Collectors.toMap(e -> extractor.apply(e), identity()));
        } catch (Exception e) {
            logger.error("getByPipeline error,", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    // TODO: 2019/12/11 太着急了得赶进度这里不抽象了 hardcode了
    public <V> Map<String, V> getByPipeline(List<String> keys, Function<String, V> decoder) {
        Jedis jedis = null;
        Map<String, V> result = new HashMap<>();
        try {

            jedis = pool.getResource();
            Pipeline pipelined = jedis.pipelined();
            keys.stream().forEach(key -> pipelined.get(key));
            List<Object> objects = pipelined.syncAndReturnAll();

            for (int i = 0; i < keys.size(); i++) {
                String s = (String)objects.get(i);
                if (StringUtils.isJedisEmpty(s)) {
                    continue;
                }
                result.put(keys.get(i), decoder.apply(s));
            }
        } catch (Exception e) {
            logger.error("getByPipeline error,", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public Map<String, Boolean> existByPipeline(List<String> keys) {
        Jedis jedis = null;
        Map<String, Boolean> result = new HashMap<>();
        try {
            jedis = pool.getResource();
            Pipeline pipelined = jedis.pipelined();
            keys.stream().forEach(key -> pipelined.exists(key));
            List<Object> objects = pipelined.syncAndReturnAll();
            List<Boolean> bools = objects.stream().map(o -> (Boolean) o).collect(Collectors.toList());
            // 返回和请求顺序是相同的。
            for (int i = 0; i < keys.size(); i++) {
                result.put(keys.get(i), bools.get(i));
            }
        } catch (Exception e) {
            logger.error("existByPipeline error,", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 默认使用json序列化
     *
     * @param content
     */
    public void setByPipeline(Map<String, Object> content) {
        if (MapUtils.isEmpty(content)) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Pipeline pipelined = jedis.pipelined();
            content.entrySet().forEach(e -> pipelined.setex(e.getKey()
                    , CacheConst.EXPIRE_TIME, JSONUtils.toJSONString(e.getValue())));
            pipelined.sync();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    // TODO: 2019/4/26 增加有expire功能的pipelineset
    public <T> void setExListByPipeline(Collection<RedisSetListModel<T>> content, Function<Collection<T>, String> encoder) {
        if (CollectionUtils.isEmpty(content)) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Pipeline pipelined = jedis.pipelined();
            for (RedisSetListModel rsm : content) {
                if (rsm.getSeconds() == -1) {
                    pipelined.set(rsm.getKey(), encoder.apply(rsm.getValue()));
                } else {
                    pipelined.setex(rsm.getKey(), rsm.getSeconds(), encoder.apply(rsm.getValue()));
                }
            }
            pipelined.sync();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }


    public String getKey(String prefix, String field) {
        if (field == null || "".equals(field)) {
            return prefix;
        } else {
            return prefix + "-" + field;
        }
    }


    /**
     * <p>通过key判断值得类型</p>
     */
    public String type(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.type(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

/*******************************************String begin***************************************************************/

    /**
     * <p>返回满足pattern表达式的所有key</p>
     * <p>keys(*)</p>
     * <p>返回所有的key</p>
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.keys(pattern);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回给定 key 的剩余生存时间
     * 以秒为单位
     */
    public Long ttl(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ttl(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * set数据
     */
    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * set 数据
     */
    public String set(String key, String value, String nxxx, String expx, long time) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value, nxxx, expx, time);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * get 数据
     */
    public String get(String key) {
        Jedis jedis = null;
        try {
            System.out.println(pool.toString());
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * get 数据
     */
    public String getTimeOutTry(String key) {
      return SafeRun.retryFuncition(this::timeOutExceptionGetByKey, key,3);
    }
    public String timeOutExceptionGetByKey(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * EVAL 命令对 Lua 脚本
     */
    public Object eval(String script, List<String> keys, List<String> args) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.eval(script, keys, args);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key向指定的value值追加值</p>
     *
     * @return 成功返回 添加后value的长度 失败 返回 添加的 value 的长度  异常返回0L
     */
    public Long append(String key, String str) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.append(key, str);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>判断key是否存在</p>
     *
     * @return true OR false
     */
    public Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>设置key value并制定这个键值的有效期</p>
     *
     * @param seconds 单位:秒
     * @return 成功返回OK 失败和异常返回null
     */
    public String setex(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 设置key value 如果key存在 将返回0 不存在返回1
     */
    public Long setnx(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setnx(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>设置key 有效期</p>
     *
     * @param seconds 单位:秒
     * @return 成功返回OK 失败和异常返回null
     */
    public Long expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key 和offset 从指定的位置开始将原先value替换</p>
     * <p>下标从0开始,offset表示从offset下标开始替换</p>
     * <p>如果替换的字符串长度过小则会这样</p>
     * <p>example:</p>
     * <p>value : bigsea@zto.cn</p>
     * <p>str : abc </p>
     * <P>从下标7开始替换  则结果为</p>
     * <p>RES : bigsea.abc.cn</p>
     *
     * @param offset 下标位置
     * @return 返回替换后  value 的长度
     */
    public Long setrange(String key, String str, int offset) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setrange(key, offset, str);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过批量的key获取批量的value</p>
     *
     * @param keys string数组 也可以是一个key
     * @return 成功返回value的集合, 失败返回null的集合 ,异常返回空
     */
    public List<String> mget(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.mget(keys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>批量的设置key:value,可以一个</p>
     * <p>example:</p>
     * <p>  obj.mset(new String[]{"key2","value1","key2","value2"})</p>
     *
     * @return 成功返回OK 失败 异常 返回 null
     */
    public String mset(String... keysvalues) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.mset(keysvalues);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>批量的设置key:value,可以一个,如果key已经存在则会失败,操作会回滚</p>
     * <p>example:</p>
     * <p>  obj.msetnx(new String[]{"key2","value1","key2","value2"})</p>
     *
     * @return 成功返回1 失败返回0
     */
    public Long msetnx(String... keysvalues) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.msetnx(keysvalues);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>设置key的值,并返回一个旧值</p>
     *
     * @return 旧值 如果key不存在 则返回null
     */
    public String getset(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getSet(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过下标 和key 获取指定下标位置的 value</p>
     *
     * @param startOffset 开始位置 从0 开始 负数表示从右边开始截取
     * @return 如果没有返回null
     */
    public String getrange(String key, int startOffset, int endOffset) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.getrange(key, startOffset, endOffset);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 有序集合操作
     * 生成有序集合id
     * <p>通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1</p>
     *
     * @return 加值后的结果
     */
    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incr(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key给指定的value加值,如果key不存在,则这是value为该值</p>
     */
    public Long incrBy(String key, Long integer) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incrBy(key, integer);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>对key的值做减减操作,如果key不存在,则设置key为-1</p>
     */
    public Long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decr(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /*******************************************Hash begin***************************************************************/

    /**
     * <p>减去指定的值</p>
     */
    public Long decrBy(String key, Long integer) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decrBy(key, integer);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key获取value值的长度</p>
     *
     * @return 失败返回null
     */
    public Long serlen(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.strlen(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * hash散列操作
     * <p>通过key给field设置指定的值,如果key不存在,则先创建</p>
     *
     * @param field 字段
     * @return 如果存在返回0 异常返回null
     */
    public Long hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0</p>
     */
    public Long hsetnx(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hsetnx(key, field, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 有序集合操作
     * <p>通过key同时设置 hash的多个field</p>
     *
     * @return 返回OK 异常返回null
     */
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmset(key, hash);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * hash散列操作
     * <p>通过key 和 field 获取指定的 value</p>
     *
     * @return 没有返回null
     */
    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hget(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key 和 fields 获取指定的value 如果没有对应的value则返回null</p>
     *
     * @param fields 可以使 一个String 也可以是 String数组
     */
    public List<String> hmget(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key给指定的field的value加上给定的值</p>
     */
    public Long hincrby(String key, String field, Long value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hincrBy(key, field, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key和field判断是否有指定的value存在</p>
     */
    public Boolean hexists(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hexists(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key返回field的数量</p>
     */
    public Long hlen(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hlen(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * hash散列操作
     * <p>通过key 删除指定的 field </p>
     *
     * @param fields 可以是 一个 field 也可以是 一个数组
     */
    public Long hdel(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hdel(key, fields);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key返回所有的field</p>
     */
    public Set<String> hkeys(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hkeys(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key返回所有和key有关的value</p>
     */
    public List<String> hvals(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hvals(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * hash散列操作
     * <p>通过key获取所有的field和value</p>
     */
    public Map<String, String> hgetall(String key) {
        Jedis jedis = null;
        try {
            if (this.exists(key)) {
                jedis = pool.getResource();
                return jedis.hgetAll(key);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /*******************************************List begin***************************************************************/

    /**
     * list列表操作
     * <p>通过key向list头部添加字符串</p>
     *
     * @param strs 可以使一个string 也可以使string数组
     * @return 返回list的value个数
     */
    public Long lpush(String key, String... strs) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, strs);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * list列表操作
     * <p>通过key向list尾部添加字符串</p>
     *
     * @param strs 可以使一个string 也可以使string数组
     * @return 返回list的value个数
     */
    public Long rpush(String key, String... strs) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpush(key, strs);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key设置list指定下标位置的value</p>
     * <p>如果下标超过list里面value的个数则报错</p>
     *
     * @param index 从0开始
     * @return 成功返回OK
     */
    public String lset(String key, Long index, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lset(key, index, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key从对应的list中删除指定的count个 和 value相同的元素</p>
     *
     * @param count 当count为0时删除全部
     * @return 返回被删除的个数
     */
    public Long lrem(String key, long count, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrem(key, count, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key保留list中从strat下标开始到end下标结束的value值</p>
     *
     * @return 成功返回OK
     */
    public String ltrim(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ltrim(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * list列表操作
     * <p>通过key从list的头部删除一个value,并返回该value</p>
     */
    public String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpop(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key从list尾部删除一个value,并返回该元素</p>
     */
    public String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpop(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value</p>
     * <p>如果第一个list为空或者不存在则返回null</p>
     */
    public String rpoplpush(String srckey, String dstkey) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpoplpush(srckey, dstkey);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * list列表操作
     * <p>通过key获取list中指定下标位置的value</p>
     *
     * @return 如果没有返回null
     */
    public String lindex(String key, long index) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lindex(key, index);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key返回list的长度</p>
     */
    public Long llen(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.llen(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * list列表操作
     * <p>通过key获取list指定下标位置的value</p>
     * <p>如果start 为 0 end 为 -1 则返回全部的list中的value</p>
     */
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /*******************************************Set begin***************************************************************/

    /**
     * set集合操作
     * <p>通过key向指定的set中添加value</p>
     *
     * @param members 可以是一个String 也可以是一个String数组
     * @return 如果存在返回0 异常返回null
     */
    public Long sadd(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, members);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * set集合操作
     * <p>通过key删除set中对应的value值</p>
     *
     * @param members 可以是一个String 也可以是一个String数组
     * @return 删除成功的数量
     */
    public Long srem(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, members);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key随机删除一个set中的value并返回该值</p>
     */
    public String spop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.spop(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key获取set中的差集</p>
     * <p>以第一个set为标准</p>
     *
     * @param keys 可以使一个string 则返回set中所有的value 也可以是string数组
     */
    public Set<String> sdiff(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sdiff(keys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key获取set中的差集并存入到另一个key中</p>
     * <p>以第一个set为标准</p>
     *
     * @param dstkey 差集存入的key
     * @param keys   可以使一个string 则返回set中所有的value 也可以是string数组
     */
    public Long sdiffstore(String dstkey, String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sdiffstore(dstkey, keys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key获取指定set中的交集</p>
     *
     * @param keys 可以使一个string 也可以是一个string数组
     */
    public Set<String> sinter(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sinter(keys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key获取指定set中的交集 并将结果存入新的set中</p>
     *
     * @param keys 可以使一个string 也可以是一个string数组
     */
    public Long sinterstore(String dstkey, String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sinterstore(dstkey, keys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key返回所有set的并集</p>
     *
     * @param keys 可以使一个string 也可以是一个string数组
     */
    public Set<String> sunion(String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sunion(keys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key返回所有set的并集,并存入到新的set中</p>
     *
     * @param keys 可以使一个string 也可以是一个string数组
     */
    public Long sunionstore(String dstkey, String... keys) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sunionstore(dstkey, keys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key将set中的value移除并添加到第二个set中</p>
     *
     * @param srckey 需要移除的
     * @param dstkey 添加的
     * @param member set中的value
     */
    public Long smove(String srckey, String dstkey, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.smove(srckey, dstkey, member);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key获取set中value的个数</p>
     */
    public Long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key获取set中随机的value,不删除元素</p>
     */
    public String srandmember(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srandmember(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * set集合操作
     * <p>通过key获取set中所有的value</p>
     */
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.smembers(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * set集合操作
     * <p>通过key判断value是否是set中的元素</p>
     */
    public Boolean sismember(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /*******************************************Sorte Set begin***************************************************************/

    /**
     * 有序集合操作
     * <p>通过key向zset中添加value,score,其中score就是用来排序的</p>
     * <p>如果该value已经存在则根据score更新元素</p>
     *
     * @return 如果存在返回0 异常返回null
     */
    public Long zadd(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 批量放入
     *
     * @param key
     * @param scoreMembers
     * @return
     */
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 有序集合操作
     * <p>通过key删除在zset中指定的value</p>
     *
     * @param members 可以使一个string 也可以是一个string数组
     */
    public Long zrem(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrem(key, members);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 有序集合操作
     * <p>通过key增加该zset中value的score的值</p>
     *
     * @param score  分值
     * @param member 成员
     */
    public Double zincrby(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zincrby(key, score, member);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0D;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key返回zset中value的排名</p>
     * <p>下标从小到大排序</p>
     */
    public Long zrank(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrank(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 有序集合操作
     * <p>通过key返回zset中value的排名</p>
     * <p>下标从大到小排序</p>
     */
    public Long zrevrank(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrank(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 有序集合操作
     * <p>通过key将获取score从start到end中zset的value</p>
     * <p>socre从大到小排序</p>
     * <p>当start为0 end为-1时返回全部</p>
     */
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 有序集合操作
     * <p>通过key返回指定score内zset中的value</p>
     */
    public Set<String> zrangeByScore(String key, double min, double max) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> zrangeByScore(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> zrevrangeByScore(String key, String max, String min) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScoreWithScores(key, min, max, 0, 1000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 有序集合操作(带score)
     * * <p>通过key返回指定score内zset中的value</p>
     */
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min, 0, 1000);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<Tuple> zrevrangeByScoreWithScoresStr(String key, String max, String min, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public Set<String> zrevrangeByScore(final String key, final double max, final double min,
                                        final int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> zrevrangeByScoreStr(final String key, final String max, final String min,
                                           final int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>返回指定区间内zset中value的数量</p>
     *
     * @param min 0
     * @param max -1
     */
    public Long zcount(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcount(key, min, max);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。
     * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。
     * 除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
     *
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
     */
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key返回zset中的value个数</p>
     */
    public Long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key获取zset中value的score值</p>
     */
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0D;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /*******************************************地理geo begin***************************************************************/

    /**
     * 有序集合操作
     * <p>通过key删除给定区间内的元素</p>
     */
    public Long zremrangeByRank(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * <p>通过key删除指定score内的元素</p>
     */
    public Long zremrangeByScore(String key, double start, double end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zremrangeByScore(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将给定的空间元素（纬度、经度、名字）添加到指定的键里面
     *
     * @param key    键
     * @param lng    经度
     * @param lat    纬度
     * @param member 成员
     */
    public Long geoadd(String key, double lng, double lat, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.geoadd(key, lng, lat, member);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取两个用户之间的距离
     *
     * @param key     键
     * @param member1 用户1
     * @param member2 用户2
     */
    public Double geodist(String key, String member1, String member2) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.geodist(key, member1, member2, GeoUnit.KM);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0D;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从键里面返回所有给定位置元素的位置（经度和纬度）。
     *
     * @param key     键
     * @param members 用户
     */
    public List<GeoCoordinate> geopos(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.geopos(key, members);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /************redis事务****************************/
    public Long getKeyIdMulti(String tableName, Long keyId, int flag) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.watch(tableName);
            Transaction transaction = jedis.multi();
            if (flag == 0L) {
                keyId += 1;
                keyId = this.incrBy(tableName, keyId);
            } else {
                keyId = this.incr(tableName);
            }
            transaction.exec();
            return keyId;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取用户附近的所有用户
     *
     * @param key    键
     * @param member 该用户
     * @param radius 范围距离
     */
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.georadiusByMember(key, member, radius, GeoUnit.KM, GeoRadiusParam.geoRadiusParam().sortAscending());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /************service缓存****************************/
    /**
     * 数据缓存
     *
     * @param key   key
     * @param field 键
     * @param value 值
     */
    public Long cachePut(String key, String field, Object value) {
        return this.cachePut(key, field, value, CacheConst.EXPIRE_TIME);
    }

    /**
     * 数据缓存 带时间
     *
     * @param key    key
     * @param field  键
     * @param value  值
     * @param exprie 过期时间
     */
    public Long cachePut(String key, String field, Object value, int exprie) {
        if (field == null || "".equals(field)) {
            this.setex(key, JSONUtils.toJSONString(value), exprie);
        } else {
            this.setex(key + "-" + field, JSONUtils.toJSONString(value), exprie);
        }
        return 1L;
    }

    /**
     * 数据缓存读取
     *
     * @param key   key
     * @param field 键
     */
    public String cacheAble(String key, String field) {
        if (field == null || "".equals(field)) {
            return this.get(key);
        } else {
            return this.get(key + "-" + field);
        }
    }

    /**
     * 删除标识开头的缓存
     *
     * @param cacheKey 标识key
     */
    public void removeKeys(String cacheKey) {
        Set<String> keys = this.keys(cacheKey + "*");
        if (keys.size() > 0) {
            for (String key : keys) {
                this.remove(key);
            }
        }
    }
}