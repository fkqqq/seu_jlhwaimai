package com.seu.waimai.bloomfilter;

public interface BloomFilterStrategy {
    /**
     * 检查元素是否存在
     * @param key 元素键
     * @return 是否存在
     */
    boolean check(String key);

    /**
     * 添加元素
     * @param key 元素键
     */
    void add(String key);
}