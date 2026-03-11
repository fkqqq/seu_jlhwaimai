package com.seu.waimai.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // 秒杀消息队列
    public static final String SECKILL_QUEUE = "seckill.queue";
    // 秒杀消息交换机
    public static final String SECKILL_EXCHANGE = "seckill.exchange";
    // 秒杀消息路由键
    public static final String SECKILL_ROUTING_KEY = "seckill.routing.key";

    // 延迟队列
    public static final String DELAY_QUEUE = "delay.queue";
    // 延迟交换机
    public static final String DELAY_EXCHANGE = "delay.exchange";
    // 延迟路由键
    public static final String DELAY_ROUTING_KEY = "delay.routing.key";

    /**
     * 创建秒杀消息队列
     */
    @Bean
    public Queue seckillQueue() {
        return new Queue(SECKILL_QUEUE, true, false, false);
    }

    /**
     * 创建秒杀消息交换机
     */
    @Bean
    public DirectExchange seckillExchange() {
        return new DirectExchange(SECKILL_EXCHANGE, true, false);
    }

    /**
     * 绑定秒杀消息队列和交换机
     */
    @Bean
    public Binding seckillBinding() {
        return BindingBuilder.bind(seckillQueue()).to(seckillExchange()).with(SECKILL_ROUTING_KEY);
    }

    /**
     * 创建延迟队列
     */
    @Bean
    public Queue delayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        // 设置队列的过期时间为30分钟
        arguments.put("x-message-ttl", 1800000);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", SECKILL_EXCHANGE);
        // 设置死信路由键
        arguments.put("x-dead-letter-routing-key", SECKILL_ROUTING_KEY);
        return new Queue(DELAY_QUEUE, true, false, false, arguments);
    }

    /**
     * 创建延迟交换机
     */
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE, true, false);
    }

    /**
     * 绑定延迟队列和交换机
     */
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(DELAY_ROUTING_KEY);
    }
}