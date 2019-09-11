package com.changgou.search.service;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @Description: 自定义高亮结果集映射
* @Author: fuYuFeng
* @Date: 2019/8/26 14:44
*/
public class SearchResultMapperImpl implements SearchResultMapper {

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> clazz, Pageable pageable) {
        //创建一个当前页的记录集合
        List<T> content = new ArrayList<>();

        if (searchResponse.getHits() == null || searchResponse.getHits().getTotalHits()<=0) {
            return new AggregatedPageImpl<>(content);
        }

        //搜索到的结果集
        for (SearchHit searchHit : searchResponse.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();
            SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);
            //获取高亮数据
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            //取出高亮字段
            HighlightField highlightField = highlightFields.get("name");
            if (highlightField != null) {
                StringBuffer stringBuffer = new StringBuffer();
                //取出高亮碎片，存到stringBuffer中
                for (Text text : highlightField.getFragments()) {
                    String string = text.string();
                    stringBuffer.append(string);
                }
                skuInfo.setName(stringBuffer.toString());
            }
            content.add((T)skuInfo);
        }
        //2.创建分页的对象 已有

        //3.获取总个记录数
        long totalHits = searchResponse.getHits().getTotalHits();

        //4.获取所有聚合函数的结果
        Aggregations aggregations = searchResponse.getAggregations();

        //5.深度分页的ID
        String scrollId = searchResponse.getScrollId();

        return new AggregatedPageImpl<T>(content,pageable,totalHits,aggregations,scrollId);
    }
}
