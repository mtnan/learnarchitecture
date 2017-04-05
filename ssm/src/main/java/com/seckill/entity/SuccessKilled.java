package com.seckill.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by Kohler on 2017/3/30.
 */
@Data
public class SuccessKilled {
    private long seckillId;
    private long userPhone;
    private short state;
    private Date createTime;

    private Seckill seckill;
}
