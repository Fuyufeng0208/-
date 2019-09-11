package com.changgou.order.controller;

import com.changgou.order.config.TokenDecode;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by fyf on 2019/9/1
 */
@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {
    @Autowired(required = false)
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 添加购物车
     * @param num
     * @param id
     * @return
     */
    @RequestMapping("/add")
    public Result add(Integer num ,Long id){
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("username");
        cartService.add(num,id,username);
        return  new Result(true, StatusCode.OK,"加入购物车成功");
    }

    /**
     * 查询购物车列表
     * @return
     */
    @RequestMapping("/list")
    public Result list(){
        Map<String, String> userInfo = tokenDecode.getUserInfo();
        String username = userInfo.get("username");
        List<OrderItem> list = cartService.findList(username);
        return new Result(true,StatusCode.OK,"查询购物车列表成功",list);
    }
}
