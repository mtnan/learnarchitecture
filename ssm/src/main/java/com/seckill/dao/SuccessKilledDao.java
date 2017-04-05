package com.seckill.dao;

import org.apache.ibatis.annotations.Param;
import com.seckill.entity.SuccessKilled;

/**
 * Created by Kohler on 2017/3/30.
 */
public interface SuccessKilledDao {
    int countAll();
    /**
     * 插入明细，过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 查询successKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
