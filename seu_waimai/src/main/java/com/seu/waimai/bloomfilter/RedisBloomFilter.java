package com.seu.waimai.bloomfilter;

import com.seu.waimai.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.BitSet;
import java.util.Random;

@Component
public class RedisBloomFilter implements BloomFilter {

    @Autowired
    private RedisUtil redisUtil;

    private String keyPrefix = "bloom:";
    private int hashFunctions = 3;
    private int size = 1000000;

    @Override
    public void add(String key) {
        for (int i = 0; i < hashFunctions; i++) {
            int position = hash(key, i);
            redisUtil.redisTemplate.opsForValue().setBit(keyPrefix + key, position, true);
        }
    }

    @Override
    public boolean contains(String key) {
        for (int i = 0; i < hashFunctions; i++) {
            int position = hash(key, i);
            Boolean exists = redisUtil.redisTemplate.opsForValue().getBit(keyPrefix + key, position);
            if (exists == null || !exists) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        // 实际项目中需要实现清除逻辑
        // 这里简化处理
    }

    /**
     * 哈希函数
     * @param key 键
     * @param seed 种子
     * @return 哈希值
     */
    private int hash(String key, int seed) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = seed * hash + key.charAt(i);
        }
        return Math.abs(hash) % size;
    }
}