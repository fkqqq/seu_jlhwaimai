package com.seu.waimai.service;

import com.seu.waimai.entity.Order;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    /**
     * 创建秒杀订单
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 订单
     */
    public Order createSeckillOrder(long userId, long couponId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setCouponId(couponId);
        order.setStatus(0); // 待支付
        order.setCreateTime(new Date());
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        // 实际项目中需要保存到数据库
        System.out.println("创建秒杀订单: " + order);
        return order;
    }

    /**
     * 检查订单支付状态
     * @param orderId 订单ID
     * @return 支付状态
     */
    public boolean checkPayStatus(long orderId) {
        // 实际项目中需要从数据库查询订单状态
        System.out.println("检查订单支付状态: " + orderId);
        // 模拟检查逻辑
        return false;
    }

    /**
     * 取消订单
     * @param orderId 订单ID
     */
    public void cancelOrder(long orderId) {
        // 实际项目中需要更新数据库订单状态
        System.out.println("取消订单: " + orderId);
    }

    /**
     * 支付订单
     * @param orderId 订单ID
     */
    public void payOrder(long orderId) {
        // 实际项目中需要更新数据库订单状态
        System.out.println("支付订单: " + orderId);
    }
}