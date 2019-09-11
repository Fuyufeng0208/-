package com.changgou.goods.service.impl;


import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.service.BrandService;
import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by fyf on 2019/8/16
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired(required = false)
    private BrandMapper brandMapper;

    /**
     * 查询所有品牌
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增品牌
     * @param brand
     */
    @Override
    public void add(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    /**
     * 修改品牌
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 删除品牌
     * @param id
     */
    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 品牌列表条件查询
     * @param brand
     * @return
     */
    @Override
    public List<Brand> findList(Brand brand) {
        Example example = createExample(brand);
        return brandMapper.selectByExample(example);
    }

    /**
     * 品牌列表分页查询
     * @param size
     * @param page
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(Integer size, Integer page) {
        //开始分页
        PageHelper.startPage(size, page);
        //查询所有
        List<Brand> list = brandMapper.selectAll();
        return new PageInfo<Brand>(list);
    }

    /**
     * 品牌列表条件+分页查询
     * @param brand
     * @param size
     * @param page
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(Brand brand, Integer size, Integer page) {
        //分页
        PageHelper.startPage(size,page);
        //按条件查询
        Example example = createExample(brand);
        List<Brand> list = brandMapper.selectByExample(example);
        return new PageInfo<Brand>(list);
    }

    @Override
    public List<Brand> findByCategory(Integer id) {
        return brandMapper.findByCategory(id);
    }


    /**
     * 构建查询对象
     * @param brand
     * @return
     */
    private Example createExample(Brand brand) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (brand!=null) {
            if (!StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name","%"+brand.getName()+"%");
            }
            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andLike("letter","%"+brand.getLetter()+"%");
            }
        }
        return example;
    }
}
