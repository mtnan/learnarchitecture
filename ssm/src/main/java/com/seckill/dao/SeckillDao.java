package com.seckill.dao;

import org.apache.ibatis.annotations.Param;
import com.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Kohler on 2017/3/30.
 */
public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 更新行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    Seckill queryById(@Param("seckillId") long seckillId);

    /**
     * 分页查询
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    void killByProcedure(Map<String, Object> map);

}
