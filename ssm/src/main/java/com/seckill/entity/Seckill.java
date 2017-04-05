package com.seckill.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Kohler on 2017/3/30.
 */
@Data
public class Seckill implements Serializable {
    private long seckillId;
    private String name;
    private int number;
    private Date startTime;
    private Date endTime;
    private Date createTime;
}
