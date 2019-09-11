package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by fyf on 2019/9/1
 */
@FeignClient(name = "user")
@RequestMapping("/user")
public interface UserFeign {
    @GetMapping("/load/{id}")
    public Result<User> findByUsername(@PathVariable(name = "id") String id);

    @GetMapping(value = "/points/add")
    public Result addPoints(@RequestParam(value = "points") Integer points, @RequestParam(value = "username") String username);
}
