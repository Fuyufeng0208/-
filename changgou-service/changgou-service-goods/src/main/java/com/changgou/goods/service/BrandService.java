package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by fyf on 2019/8/16
 */

public interface BrandService {
    /**
     * 查询所有品牌
     * @return
     */
    List<Brand> findAll();

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    Brand findById(Integer id);

    /**
     * 新增品牌
     * @param brand
     */
    void add(Brand brand);

    /**
     * 修改品牌
     * @param brand
     */
    void update(Brand brand);

    /**
     * 删除品牌
     * @param id
     */
    void delete(Integer id);

    /**
     * 品牌列表条件查询
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);

    /**
     * 品牌列表分页查询
     * @param size
     * @param page
     * @return
     */
    PageInfo<Brand> findPage(Integer size, Integer page);

    /**
     * 品牌列表条件+分页查询
     * @param brand
     * @param size
     * @param page
     * @return
     */
    PageInfo<Brand> findPage(Brand brand, Integer size, Integer page);

    /**
     * 查询品牌列表成功
     * @param id
     * @return
     */
    List<Brand> findByCategory(Integer id);
}
