package com.seu.waimai.bloomfilter;

public interface BloomFilter {
    /**
     * 添加元素到布隆过滤器
     * @param key 元素键
     */
    void add(String key);

    /**
     * 检查元素是否可能存在于布隆过滤器中
     * @param key 元素键
     * @return 是否可能存在
     */
    boolean contains(String key);

    /**
     * 清除布隆过滤器
     */
    void clear();
}