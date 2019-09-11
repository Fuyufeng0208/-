package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.service.CateGoryService;
import com.changgou.goods.pojo.Category;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by fyf on 2019/8/20
 */
@Service
public class CateGoryServiceImpl implements CateGoryService {
    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    /**
     * 多条件分页查询
     * @param category
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Category> findPage(Category category, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(category);
        List<Category> categories = categoryMapper.selectByExample(example);
        return new PageInfo<>(categories);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Category> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        List<Category> categories = categoryMapper.selectAll();
        return new PageInfo<>(categories);
    }

    /**
     * 多条件搜索方法
     * @param category
     * @return
     */
    @Override
    public List<Category> findList(Category category) {
        Example example = createExample(category);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void delete(Integer id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param category
     */
    @Override
    public void update(Category category) {
        categoryMapper.updateByPrimaryKey(category);
    }

    /**
     * 添加
     * @param category
     */
    @Override
    public void add(Category category) {
        categoryMapper.insertSelective(category);
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    @Override
    public Category findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询全部
     * @return
     */
    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    /**
     * 根据父节点id查询
     * @param pid:父节点ID
     * @return
     */
    @Override
    public List<Category> findByParentId(Integer pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    /**
     * 构建查询对象
     * @param category
     */
    private Example createExample(Category category) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if (category!=null) {
            // 分类名称
            if(!StringUtils.isEmpty(category.getName())){
                criteria.andLike("name","%"+category.getName()+"%");
            }
            // 商品数量
            if(!StringUtils.isEmpty(category.getGoodsNum())){
                criteria.andEqualTo("goodsNum",category.getGoodsNum());
            }
            // 是否显示
            if(!StringUtils.isEmpty(category.getIsShow())){
                criteria.andEqualTo("isShow",category.getIsShow());
            }
            // 是否导航
            if(!StringUtils.isEmpty(category.getIsMenu())){
                criteria.andEqualTo("isMenu",category.getIsMenu());
            }
            // 排序
            if(!StringUtils.isEmpty(category.getSeq())){
                criteria.andEqualTo("seq",category.getSeq());
            }
            // 上级ID
            if(!StringUtils.isEmpty(category.getParentId())){
                criteria.andEqualTo("parentId",category.getParentId());
            }
            // 模板ID
            if(!StringUtils.isEmpty(category.getTemplateId())){
                criteria.andEqualTo("templateId",category.getTemplateId());
            }
        }
        return example;
    }
}
