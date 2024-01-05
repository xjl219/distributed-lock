package com.xjl.locks.utils;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockClientFactory {
    private static final Map<String, RedissonClient> clientMap = new ConcurrentHashMap();

    public LockClientFactory() {
    }

    public static RedissonClient getRedissonClient(){
       return getRedissonClient(LockContextManager.getContext().getServiceCode());
    }
    public static RedissonClient getRedissonClient(String serviceCode) {
        RedissonClient redissonClient = (RedissonClient) clientMap.get(serviceCode);
        if (redissonClient == null) {
            synchronized (serviceCode.intern()) {
                redissonClient = (RedissonClient) clientMap.get(serviceCode);
                if (redissonClient == null) {
                    redissonClient = createRedissonClient(serviceCode);
                    clientMap.put(serviceCode, redissonClient);
                }
            }
        }

        return redissonClient;
    }

    private static RedissonClient createRedissonClient(String serviceCode) {
        return getRedissonClient(serviceCode);
    }
    private static RedissonClient getRedissonClient0(String serviceCode) {
        try {


            try {
                Config config = new Config();
                RedissonClient redissonClient = Redisson.create(config);
                return redissonClient;
            } catch (Exception var6) {
                throw new RuntimeException("根据配置信息获取RedissonClient失败。配置或Redis服务的可用性。", var6);
            }
        } catch (Exception var10) {
            throw new RuntimeException(var10);
        }
    }
}