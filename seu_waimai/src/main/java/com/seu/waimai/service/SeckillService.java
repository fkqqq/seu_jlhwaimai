package com.seu.waimai.service;

import com.seu.waimai.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeckillService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 秒杀库存Lua脚本
    private static final String SECKILL_LUA_SCRIPT = """
        local stockKey = KEYS[1]
        local userKey = KEYS[2]
        local userId = ARGV[1]
        local stock = tonumber(redis.call('get', stockKey))
        if stock == nil then
            return -1
        end
        if stock <= 0 then
            return 0
        end
        if redis.call('sismember', userKey, userId) == 1 then
            return -2
        end
        redis.call('decr', stockKey)
        redis.call('sadd', userKey, userId)
        return 1
    """;

    /**
     * 执行秒杀
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @return 秒杀结果：1-成功，0-库存不足，-1-商品不存在，-2-用户已抢购
     */
    public int seckill(long couponId, long userId) {
        String stockKey = "seckill:stock:" + couponId;
        String userKey = "seckill:user:" + couponId;

        // 执行Lua脚本
        List<String> keys = new ArrayList<>();
        keys.add(stockKey);
        keys.add(userKey);
        Object result = redisTemplate.execute(
                new org.springframework.data.redis.core.script.DefaultRedisScript<>(SECKILL_LUA_SCRIPT, Integer.class),
                keys,
                String.valueOf(userId)
        );

        return result != null ? Integer.parseInt(result.toString()) : -1;
    }

    /**
     * 初始化秒杀库存
     * @param couponId 优惠券ID
     * @param stock 库存数量
     */
    public void initStock(long couponId, int stock) {
        String stockKey = "seckill:stock:" + couponId;
        redisUtil.set(stockKey, stock, 86400);
    }

    /**
     * 检查用户是否已抢购
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @return 是否已抢购
     */
    public boolean hasSeckilled(long couponId, long userId) {
        String userKey = "seckill:user:" + couponId;
        return redisTemplate.opsForSet().isMember(userKey, userId);
    }

    /**
     * 获取当前库存
     * @param couponId 优惠券ID
     * @return 库存数量
     */
    public int getStock(long couponId) {
        String stockKey = "seckill:stock:" + couponId;
        Object stock = redisUtil.get(stockKey);
        return stock != null ? Integer.parseInt(stock.toString()) : 0;
    }
}