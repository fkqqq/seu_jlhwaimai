package com.seu.waimai.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private Long id;
    private Long userId;
    private Long couponId;
    private Integer status; // 0-待支付，1-已支付，2-已取消
    private Date createTime;
    private Date payTime;
    private String orderNo;
}