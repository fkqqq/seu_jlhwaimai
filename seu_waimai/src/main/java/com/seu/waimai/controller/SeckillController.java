package com.seu.waimai.controller;

import com.seu.waimai.service.RabbitMQService;
import com.seu.waimai.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RabbitMQService rabbitMQService;

    /**
     * 执行秒杀
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @return 秒杀结果
     */
    @PostMapping("/execute")
    public String executeSeckill(@RequestParam long couponId, @RequestParam long userId) {
        // 执行秒杀
        int result = seckillService.seckill(couponId, userId);
        
        switch (result) {
            case 1:
                // 秒杀成功，发送秒杀消息
                String seckillMessage = userId + ":" + couponId;
                rabbitMQService.sendSeckillMessage(seckillMessage);
                
                // 发送延迟消息，检查支付状态
                // 这里简化处理，实际项目中需要获取订单ID
                String delayMessage = "1";
                rabbitMQService.sendDelayMessage(delayMessage);
                
                return "秒杀成功";
            case 0:
                return "库存不足";
            case -1:
                return "商品不存在";
            case -2:
                return "您已抢购过该商品";
            default:
                return "秒杀失败";
        }
    }

    /**
     * 初始化秒杀库存
     * @param couponId 优惠券ID
     * @param stock 库存数量
     * @return 初始化结果
     */
    @PostMapping("/init")
    public String initStock(@RequestParam long couponId, @RequestParam int stock) {
        seckillService.initStock(couponId, stock);
        return "库存初始化成功";
    }

    /**
     * 获取当前库存
     * @param couponId 优惠券ID
     * @return 库存数量
     */
    @PostMapping("/stock")
    public int getStock(@RequestParam long couponId) {
        return seckillService.getStock(couponId);
    }
}