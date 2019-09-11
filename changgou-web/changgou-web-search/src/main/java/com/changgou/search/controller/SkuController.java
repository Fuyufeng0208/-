package com.changgou.search.controller;



import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by fyf on 2019/8/27
 */
@Controller
@RequestMapping("/search")
public class SkuController {
    @Autowired
    private SkuFeign skuFeign;

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping("/list")
    public String search(@RequestParam(required = false) Map searchMap, Model model){
        //调用changgou-service-search微服务
        Map resultmap = skuFeign.search(searchMap);
        model.addAttribute("result", resultmap);

        //搜索条件回显
        model.addAttribute("searchMap",searchMap);

        //记住之前的url
        String url = url(searchMap);
        model.addAttribute("url",url);

        //创建分页对象
        Long total = Long.valueOf(resultmap.get("total").toString());
        Integer pageNum = 1;
        Integer pageSize = 30;
        Page<SkuInfo> infoPage = new Page<SkuInfo>(total,pageNum,pageSize);

        model.addAttribute("page",infoPage);

        return "search";
    }


    /**
     * 拼接搜条件
     * @param searchMap
     * @return
     */
    private String url(Map<String,String> searchMap) {
        String url = "/search/list";
        if (searchMap != null &&searchMap.size()>0) {
            url+="?";
            for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
                String key = stringStringEntry.getKey();
                String value = stringStringEntry.getValue();
                url+=key+"="+value+"&";
            }
            if (url.lastIndexOf("&")!=-1) {
                url=url.substring(0,url.lastIndexOf("&"));
            }
        }
        return url;
    }
}
