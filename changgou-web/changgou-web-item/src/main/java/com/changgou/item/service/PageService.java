package com.changgou.item.service;

/**
 * Created by fyf on 2019/8/28
 */
public interface PageService {
    /**
     * 根据商品的ID 生成静态页
     * @param spuId
     */
    public void createPageHtml(Long spuId) ;
}
