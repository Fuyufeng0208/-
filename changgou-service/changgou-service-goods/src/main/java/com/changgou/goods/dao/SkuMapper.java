package com.changgou.goods.dao;

import com.changgou.goods.pojo.Sku;


import com.changgou.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by fyf on 2019/8/22
 */
public interface SkuMapper extends Mapper<Sku> {
    @Update(value="update tb_sku set num=num-#{num},sale_num=sale_num+#{num} where id =#{skuId} and num >=#{num}")
    public int decrCount(OrderItem orderItem);
}
