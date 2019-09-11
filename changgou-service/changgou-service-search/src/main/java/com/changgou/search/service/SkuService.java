package com.changgou.search.service;

import java.util.Map;

public interface SkuService {

    /**
     * 数据导入
     */
    void  importEs();

    /**
     * 搜索数据
     * @param searchMap
     * @return
     */
    Map search(Map<String, String> searchMap);
}
