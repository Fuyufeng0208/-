package com.changgou.seckill.timer;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by fyf on 2019/9/7
 */
@Component
public class SeckillGoodsPushTask {

    @Autowired(required = false)
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /****
     * 每5秒执行一次
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void loadGoodsPushRedis(){
        //1、利用工具类获取当前时间对应的5个时间段
        List<Date> list = DateUtil.getDateMenus();
        //2.使用工具类循环遍历5个时间段 获取到时间的日期
        for (Date starttime : list) {
            String extName =  DateUtil.data2str(starttime,DateUtil.PATTERN_YYYYMMDDHH);
            //3.将循环到的时间段 作为条件 从数据库中执行查询 得出数据集
            //select * from tb_seckill_goods where stock_count>0 and `status`='1' and start_time > 开始时间段 and end_time < 开始时间段+2hour  and id  not in (redis中已有的id)
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status","1");                                 //`status`= 1'
            criteria.andGreaterThan("stockCount",0);                           //stock_count>0
            criteria.andGreaterThanOrEqualTo("startTime",starttime);                 //start_time > 开始时间段
            criteria.andLessThan("endTime",DateUtil.addDateHour(starttime, 2));//end_time < 开始时间段+2hour
            Set keys = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + extName).keys();  //id  not in (redis中已有的id)
            if(keys!=null && keys.size()>0) {
                criteria.andNotIn("id", keys);
            }
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);

            //4.将数据集存储到redis中(key field value的数据格式 )
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + extName).put(seckillGood.getId(),seckillGood);
                //设置过期时间
                redisTemplate.expireAt(SystemConstants.SEC_KILL_GOODS_PREFIX + extName,DateUtil.addDateHour(starttime, 2));

                //商品压入队列
                pushGoods(seckillGood);
                //添加一个计数器 (key:商品的ID  value : 库存数)
                redisTemplate.boundHashOps(SystemConstants.SECK_KILL_GOODS_COUNT_KEY).increment(seckillGood.getId(),seckillGood.getStockCount());
            }
        }
    }
    public void pushGoods(SeckillGoods seckillGoods){
        //创建redis的队列(每一种商品就是一个队列,队列的元素的个数和商品的库存一致) 压入队列
        for (Integer i = 0; i < seckillGoods.getStockCount(); i++) {
            redisTemplate.boundListOps(SystemConstants.SEC_KILL_CHAOMAI_LIST_KEY_PREFIX + seckillGoods.getId()).leftPush(seckillGoods.getId());
        }
    }
}
