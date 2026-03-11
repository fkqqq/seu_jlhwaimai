package com.seu.waimai.listener;

import com.seu.waimai.config.RabbitMQConfig;
import com.seu.waimai.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeckillMessageListener {

    @Autowired
    private OrderService orderService;

    /**
     * 处理秒杀消息
     * @param message 消息内容，格式：userId:couponId
     */
    @RabbitListener(queues = RabbitMQConfig.SECKILL_QUEUE)
    public void handleSeckillMessage(String message) {
        // 处理秒杀消息，创建订单
        System.out.println("处理秒杀消息: " + message);
        try {
            String[] parts = message.split(":");
            long userId = Long.parseLong(parts[0]);
            long couponId = Long.parseLong(parts[1]);
            orderService.createSeckillOrder(userId, couponId);
        } catch (Exception e) {
            System.err.println("处理秒杀消息失败: " + e.getMessage());
        }
    }

    /**
     * 处理延迟消息（检查支付状态）
     * @param message 消息内容，格式：orderId
     */
    @RabbitListener(queues = RabbitMQConfig.DELAY_QUEUE)
    public void handleDelayMessage(String message) {
        // 处理延迟消息，检查订单支付状态
        System.out.println("处理延迟消息: " + message);
        try {
            long orderId = Long.parseLong(message);
            boolean payStatus = orderService.checkPayStatus(orderId);
            if (!payStatus) {
                orderService.cancelOrder(orderId);
                System.out.println("订单超时未支付，已取消: " + orderId);
            }
        } catch (Exception e) {
            System.err.println("处理延迟消息失败: " + e.getMessage());
        }
    }
}