package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fyf on 2019/9/1
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    private SkuFeign skuFeign;

    @Autowired(required = false)
    private SpuFeign spuFeign;
    /**
     * 添加购物车
     * @param num
     * @param id
     * @param username
     */
    @Override
    public void add(Integer num, Long id, String username) {
        if (num<=0) {
            redisTemplate.boundHashOps("Cate_"+username).delete(id);
            return;
        }
        //根据商品SKU的Id获取SKU的数据
        Result<Sku> skuResult = skuFeign.findById(id);
        Sku data = skuResult.getData();
        if (data != null) {
            //根据SKU数据获取SKU对应Spu的数据
            Long spuId = data.getSpuId();

            Result<Spu> spuResult = spuFeign.findById(spuId);
            Spu spu = spuResult.getData();

            //将数据存储到购物车对象（Order_item）
            OrderItem orderItem = new OrderItem();

            orderItem.setCategoryId1(spu.getCategory1Id());
            orderItem.setCategoryId2(spu.getCategory2Id());
            orderItem.setCategoryId3(spu.getCategory3Id());
            orderItem.setSpuId(spu.getId());
            orderItem.setSkuId(id);
            orderItem.setName(data.getName());//商品的名称  sku的名称
            orderItem.setPrice(data.getPrice());//sku的单价
            orderItem.setNum(num);//购买的数量
            orderItem.setMoney(orderItem.getNum() * orderItem.getPrice());//单价* 数量
            orderItem.setPayMoney(orderItem.getNum() * orderItem.getPrice());//单价* 数量
            orderItem.setImage(data.getImage());//商品的图片地址
            //数据添加到redis中  key:用户名 field:sku的ID  value:购物车数据(order_item)
            redisTemplate.boundHashOps("Cart_"+username).put(id,orderItem);
        }
    }

    /**
     * 购物车列表展示
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> findList(String username) {
        List list = redisTemplate.boundHashOps("Cart_" + username).values();
        return list;
    }
}
