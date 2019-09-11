package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

/**
 * Created by fyf on 2019/9/1
 */
public interface CartService {
    /**
     * 添加购物车
     * @param num
     * @param id
     * @param username
     */
    void add(Integer num, Long id, String username);

    /**
     * 购物车列表展示
     * @param username
     * @return
     */
    List<OrderItem> findList(String username);
}
