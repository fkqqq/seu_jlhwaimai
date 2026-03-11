package com.seu.waimai.service;

import com.seu.waimai.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class CacheService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取缓存数据，如果缓存不存在则从数据源获取并缓存
     * @param key 缓存键
     * @param expireTime 过期时间（秒）
     * @param supplier 数据源提供者
     * @param <T> 数据类型
     * @return 数据
     */
    public <T> T getOrSetCache(String key, long expireTime, Supplier<T> supplier) {
        // 尝试从缓存获取
        Object value = redisUtil.get(key);
        if (value != null) {
            return (T) value;
        }
        
        // 缓存不存在，从数据源获取
        T data = supplier.get();
        if (data != null) {
            // 将数据存入缓存
            redisUtil.set(key, data, expireTime);
        }
        return data;
    }

    /**
     * 清除指定缓存
     * @param key 缓存键
     */
    public void clearCache(String key) {
        redisUtil.delete(key);
    }

    /**
     * 清除匹配模式的缓存
     * @param pattern 键模式，如 "user:*"
     */
    public void clearCacheByPattern(String pattern) {
        // 实际项目中需要实现通过模式匹配删除缓存的逻辑
        // 这里简化处理
    }
}