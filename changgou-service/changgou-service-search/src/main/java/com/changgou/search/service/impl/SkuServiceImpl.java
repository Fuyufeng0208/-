package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchResultMapperImpl;
import com.changgou.search.service.SkuService;
import entity.Result;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
* @Description:
* @Param:
* @return:
* @Author: fuYuFeng
* @Date: 2019/8/25 19:30
*/
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    /**
     * 数据导入
     */
    @Override
    public void importEs() {
        //1.调用 goods微服务的fegin 查询 符合条件的sku的数据
        Result<List<Sku>> skuResult = skuFeign.findByStatus("1");
        List<Sku> data = skuResult.getData();//sku的列表
        //将sku的列表 转换成es中的skuinfo的列表
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(data), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            //获取规格的数据

            //转成MAP  key: 规格的名称  value:规格的选项的值
            Map<String, Object> map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }
        // 2.调用spring data elasticsearch的API 导入到ES中
        skuEsMapper.saveAll(skuInfos);
    }


    /**
     * 数据搜索
     * @param searchMap
     * @return
     */
    @Override
    public Map search(Map<String, String> searchMap) {
        //1.获取到关键字
        String keywords = searchMap.get("keywords");

        //2.判断是否为空 如果为空给一个默认
        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";
        }

        //3.创建 查询构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //4.设置查询的条件
        addAggregation(nativeSearchQueryBuilder);


        //设置高亮字段
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));
        //设置前缀和后缀
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder().preTags("<em style=\"color:red\">").postTags("</em>"));



        //匹配查询  先分词再查询  主条件查询
        //nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name",keywords));
        //从多个字段中搜索数据
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords,"name","categoryName","brandName"));

        //过滤查询
        conditionSearch(searchMap, nativeSearchQueryBuilder);


        //排序操作
        String sortField = searchMap.get("sortField");
        String sortRule = searchMap.get("sortRule");
        if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortRule.equalsIgnoreCase("asc")? SortOrder.ASC:SortOrder.DESC));
        }

        //5.构建查询对象(封装了查询的语法)
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();

        //6.执行查询
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(nativeSearchQuery, SkuInfo.class,new SearchResultMapperImpl());



        //6.1获取商品分类的列表数据
        StringTerms stringTermsCategory = (StringTerms) skuInfos.getAggregation("skuCategorygroup");
        List<String> categoryList = getStringsCategoryList(stringTermsCategory);

        //6.2获取商品品牌分类的列表数据
        StringTerms stringTermsBrand = (StringTerms) skuInfos.getAggregation("skuBrandgroup");
        List<String> brandList = getStringBrandList(stringTermsBrand);

        //6.2获取商品规格的列表数据
        StringTerms stringTermsSpec = (StringTerms) skuInfos.getAggregation("skuSpecgroup");
        Map<String, Set<String>> specMap = getStringSetMap(stringTermsSpec);

        //7.获取结果  返回map
        return resultMap(skuInfos, categoryList, brandList, specMap);


    }

    /**
     * 设置查询条件
     * @param nativeSearchQueryBuilder
     */
    private void addAggregation(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        //4.1设置分组条件——商品分类
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("categoryName").size(50));
        //4.2设置分组条件——商品品牌分类
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgroup").field("brandName").size(50));
        //4.3设置分组条件——商品规格分类
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(50));
    }

    /**
     * 条件筛选  分类搜索、规格搜索、价格搜索、排序
     * @param searchMap
     * @param nativeSearchQueryBuilder
     */
    public void conditionSearch(Map<String, String> searchMap, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //商品分类的条件
        String category = searchMap.get("category");
        if (!StringUtils.isEmpty(category)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("categoryName",category));
        }
        //商品品牌的条件
        String brand = searchMap.get("brand");
        if (!StringUtils.isEmpty(brand)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("brandName",brand));
        }
        //商品规格的条件
        if(searchMap!=null){
            for (String key : searchMap.keySet()) {//{ brand:"",category:"",spec_网络:"电信4G"}
                if(key.startsWith("spec_"))  {
                    //截取规格的名称
                    boolQueryBuilder.filter(QueryBuilders.termQuery("specMap."+key.substring(5)+".keyword", searchMap.get(key)));
                }
            }
        }
        //价格区间的过滤查询
        String price = searchMap.get("price");
        if (!StringUtils.isEmpty(price)) {
            String[] split = price.split("-");
            if (!split[1].equals("*")) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0],true).to(split[1],true));
            }else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            }
        }
        //分页查询
        String pageNum1 = searchMap.get("pageNum");
        Integer pageNum=Integer.valueOf(pageNum1);
        Integer pageSize=30;
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum-1,pageSize));

        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
    }


    /**
     * 结果集的封装
     * @param skuInfos
     * @param categoryList
     * @param brandList
     * @param specMap
     * @return
     */
    private Map resultMap(AggregatedPage<SkuInfo> skuInfos, List<String> categoryList, List<String> brandList, Map<String, Set<String>> specMap) {


        List<SkuInfo> content = skuInfos.getContent();
        int totalPages = skuInfos.getTotalPages();
        long totalElements = skuInfos.getTotalElements();

        Map<String,Object> resultMap =new HashMap<>();

        resultMap.put("categoryList",categoryList);//商品分类的列表数据
        resultMap.put("brandList",brandList);//商品品牌分类的列表数据
        resultMap.put("specList",specMap);//商品规格的列表数据
        resultMap.put("rows",content);//当前的页的集合
        resultMap.put("total",totalElements);//总记录数
        resultMap.put("totalPages",totalPages);//总页数

        return resultMap;
    }

    /**
     * 获取规格列表数据
     * @param stringTermsSpec
     * @return
     */
    private Map<String, Set<String>> getStringSetMap(StringTerms stringTermsSpec) {
        Map<String, Set<String>> specMap = new HashMap<String, Set<String>>();
        Set<String> specValues = new HashSet<String>();
        if (stringTermsSpec != null) {
            //获取分组的结果集，遍历出每一条数据
            for (StringTerms.Bucket bucket : stringTermsSpec.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();
                //转成JSON对象
                Map<String,String> map = JSON.parseObject(keyAsString, Map.class);
                for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                    String key = stringStringEntry.getKey();//规格名称
                    String value = stringStringEntry.getValue();//对应的单个选项值

                    specValues=specMap.get(key);
                    if (specValues==null) {
                        specValues=new HashSet<>();
                    }
                    specValues.add(value);
                    //提取map中的值放入到返回的map中
                    specMap.put(key,specValues);
                }
            }
        }
        return specMap;
    }

    /**获取品牌列表数据
     * getStringsBrandList
     * @param stringTermsBrand
     * @return
     */
    private List<String> getStringBrandList(StringTerms stringTermsBrand) {
        List<String> brandList = new ArrayList<>();
        if (stringTermsBrand != null) {
            for (StringTerms.Bucket bucket : stringTermsBrand.getBuckets()) {
                //品牌名称，分组后已经去重
                String keyAsString = bucket.getKeyAsString();
                brandList.add(keyAsString);
            }
        }
        return brandList;
    }

    /**
     * 获取分类列表数据
     * @param stringTermsCategory
     * @return
     */
    private List<String> getStringsCategoryList(StringTerms stringTermsCategory) {
        List<String> categoryList = new ArrayList<>();
        if (stringTermsCategory != null) {
            for (StringTerms.Bucket bucket : stringTermsCategory.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();
                categoryList.add(keyAsString);
            }
        }
        return categoryList;
    }
}
