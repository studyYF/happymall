package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Created by YangFan.
 * @date 2019/11/27
 * 功能:
 */
public class RedisPool {
    /**
     * jedis连接池
     */
    private static JedisPool pool;
    /**
     * 最大连接数
     */
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max" +
            ".total", "20"));

    /**
     * 最大空闲连接池个数
     */
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle"
            , "10"));

    /**
     * 最小空闲连接池个数
     */
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle"
            , "2"));

    /**
     * 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true，则jedis实例肯定是可以用的
     */
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis" +
            ".test.borrow", "true"));

    /**
     * 在return一个jedis实例的时候，是否要进行验证操作，如果赋值true，则jedis实例肯定是可以放回的
     */
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis" +
            ".test.return", "true"));


    /**
     * redis的ip
     */
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");

    /**
     * redis的port
     */
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port",
            "6379"));


    private static void initPool() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时，默认为true
        config.setBlockWhenExhausted(true);
        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    static {
        initPool();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("crossyf","crossyf");
        returnResource(jedis);
    }




}
