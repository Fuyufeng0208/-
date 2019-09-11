package com.changgou.seckill.task;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.*;
import entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by fyf on 2019/9/8
 */
@Component
public class MultiThreadingCreateOrder {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired(required = false)
    private SeckillGoodsMapper seckillGoodsMapper;
    /***
     * 多线程下单操作
     */
    @Async
    public void createOrder() {

        //从redis队列中获取订单信息
        SeckillStatus Goods = (SeckillStatus) redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).rightPop();

        if (Goods != null) {
            //测试用，数据先写死
            String time = Goods.getTime();
            Long id = Goods.getGoodsId();
            String username = Goods.getUsername();

            //判断 先从队列中获取商品 ,如果能获取到,说明 有库存,如果获取不到,说明 没库存 卖完了 return.
            Object o = redisTemplate.boundListOps(SystemConstants.SEC_KILL_CHAOMAI_LIST_KEY_PREFIX + id).rightPop();
            if ( o==null){
                //清除 掉  防止重复排队的key
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_QUEUE_REPEAT_KEY).delete(username);
                //清除 掉  排队标识(存储用户的抢单信息)
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).delete(username);
                //已售罄!直接返回
                return;
            }

            //获取商品数据
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id);

            //创建订单
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());
            seckillOrder.setSeckillId(id);
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            seckillOrder.setUserId(username);
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setStatus("0");

            //将秒杀订单存入到Redis中
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).put(username, seckillOrder);

            //库存减少
            //seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
            Long increment = redisTemplate.boundHashOps(SystemConstants.SECK_KILL_GOODS_COUNT_KEY).increment(id, -1L);
            seckillGoods.setStockCount(increment.intValue());

            //判断当前商品是否还有库存
            if (seckillGoods.getStockCount() <= 0) {
                //将商品同步到MySQL中
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                //清除redis中的数据
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX).delete(id);
            } else {
                //如果有库存，则把数据更新（减了库存后的数据）到Redis中
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).put(id, seckillGoods);
            }

            //创建订单成功了 修改用户的抢单的信息
            Goods.setOrderId(seckillOrder.getId());
            Goods.setStatus(2);//
            Goods.setMoney(Float.valueOf(seckillOrder.getMoney()));
            //重新设置回redis中
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).put(username,Goods);
        }
    }
}
