package com.changgou.goods.service.impl;

import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.service.TemplateService;
import com.changgou.goods.pojo.Template;
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
public class TemplateServiceImpl implements TemplateService {

    @Autowired(required = false)
    private TemplateMapper templateMapper;


    /**
     * 条件+分页查询
     * @param template
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Template> findPage(Template template, int page, int size) {
        //开始分页
        PageHelper.startPage(page,size);
        Example example = createExample(template);
        //按条件查询数据
        List<Template> templates = templateMapper.selectByExample(example);
        return new PageInfo<Template>(templates);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Template> findPage(int page, int size) {
        //开始分页
        PageHelper.startPage(page,size);
        //查询所有
        List<Template> templates = templateMapper.selectAll();
        return new PageInfo<>(templates);
    }

    /**
     * 根据条件查询template
     * @param template
     * @return
     */
    @Override
    public List<Template> findList(Template template) {
        Example example = createExample(template);
        List<Template> templates = templateMapper.selectByExample(example);
        return templates;
    }


    /**
     * 删除template
     * @param id
     */
    @Override
    public void delete(Integer id) {
        templateMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改template
     * @param template
     */
    @Override
    public void update(Template template) {
        templateMapper.updateByPrimaryKey(template);
    }

    /**
     * 添加template
     * @param template
     */
    @Override
    public void add(Template template) {
        templateMapper.insertSelective(template);
    }

    /**
     * 根据ID查询Template
     * @param id
     * @return
     */
    @Override
    public Template findById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有Template
     * @return
     */
    @Override
    public List<Template> findAll() {
        return templateMapper.selectAll();
    }

    /**
     * 构建查询对象
     * @param template
     */
    private Example createExample(Template template) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if (template!=null) {
            if (!StringUtils.isEmpty(template.getName())) {
                criteria.andLike("name","%"+template.getName()+"%");
            }
            if (!StringUtils.isEmpty(template.getParaNum())) {
                criteria.andLike("paraNum","%"+template.getParaNum()+"%");
            }
            if (!StringUtils.isEmpty(template.getParaNum())) {
                criteria.andLike("specNum","%"+template.getSpecNum()+"%");
            }
        }
        return example;
    }
}
