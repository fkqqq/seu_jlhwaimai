package com.seu.waimai.bloomfilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CachePenetrationBloomFilterStrategy implements BloomFilterStrategy {

    @Autowired
    private BloomFilterFactory bloomFilterFactory;

    private BloomFilter bloomFilter;

    public CachePenetrationBloomFilterStrategy() {
        // 初始化布隆过滤器
        // 实际项目中应该在构造函数中通过工厂创建
    }

    @Override
    public boolean check(String key) {
        if (bloomFilter == null) {
            bloomFilter = bloomFilterFactory.createBloomFilter("redis");
        }
        return bloomFilter.contains(key);
    }

    @Override
    public void add(String key) {
        if (bloomFilter == null) {
            bloomFilter = bloomFilterFactory.createBloomFilter("redis");
        }
        bloomFilter.add(key);
    }
}