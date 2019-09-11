package com.changgou.search.controller;

import com.changgou.search.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/**
* @Description:
* @Param:
* @return:
* @Author: fuYuFeng
* @Date: 2019/8/25 19:30
*/
@RestController
@CrossOrigin
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 导入数据
     * @return
     */
    @RequestMapping("/import")
    public Result importEs() {
        skuService.importEs();
        return new Result(true, StatusCode.OK, "导入成功");
    }

    /**
     * 搜索数据
     * @param searchMap
     * @return
     */
    @GetMapping
    public Map search(@RequestParam(required = false) Map searchMap){
        Object pageNum = searchMap.get("pageNum");
        if (pageNum==null) {
            searchMap.put("pageNum","1");
        }
        if (pageNum instanceof Integer) {
            searchMap.put("pageNum",pageNum.toString());
        }
        return  skuService.search(searchMap);
    }
}
