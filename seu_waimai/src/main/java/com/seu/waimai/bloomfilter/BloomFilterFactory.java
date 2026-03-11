package com.seu.waimai.bloomfilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BloomFilterFactory {

    @Autowired
    private RedisBloomFilter redisBloomFilter;

    /**
     * 根据类型创建布隆过滤器
     * @param type 布隆过滤器类型
     * @return 布隆过滤器实例
     */
    public BloomFilter createBloomFilter(String type) {
        switch (type) {
            case "redis":
                return redisBloomFilter;
            // 可以添加其他类型的布隆过滤器实现
            default:
                return redisBloomFilter;
        }
    }
}