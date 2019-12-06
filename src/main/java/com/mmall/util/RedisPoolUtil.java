package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Created by YangFan.
 * @date 2019/11/27
 * 功能:
 */
@Slf4j
public class RedisPoolUtil {

    /**
     * 设置key的有效期，单位是秒
     *
     * @param key    key
     * @param exTime 有效时间
     * @return 结果
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} error", key, e);
            RedisPool.returnResource(jedis);
            return null;
        }
        return result;
    }


    /**
     * 设置值
     *
     * @param key    key
     * @param value  值
     * @param exTime 保存时间，单位为秒
     * @return
     */
    public static String setEx(String key, String value, int exTime) {
        Jedis jedis = null;
        String result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error", key, value, e);
            RedisPool.returnResource(jedis);
            return null;
        }

        return result;
    }

    public static String set(String key, String value) {
        Jedis jedis = null;
        String result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnResource(jedis);
            return null;
        }

        return result;
    }

    public static String get(String key) {
        Jedis jedis = null;
        String result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error", key, e);
            RedisPool.returnResource(jedis);
            return null;
        }

        return result;
    }

    public static Long del(String key) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisPool.returnResource(jedis);
            return null;
        }

        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        RedisPoolUtil.set("imooc","geely");
        System.out.println(RedisPoolUtil.get("imooc"));

        RedisPoolUtil.setEx("1000ms","1000ms",10);

        RedisPoolUtil.expire("imooc",50);

        RedisPoolUtil.set("ttt","333");

        RedisPoolUtil.del("ttt");

    }


}
