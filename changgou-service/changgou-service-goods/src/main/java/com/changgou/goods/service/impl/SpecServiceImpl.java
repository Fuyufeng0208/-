package com.changgou.goods.service.impl;

import com.changgou.goods.dao.SpecMapper;
import com.changgou.goods.service.SpecService;
import com.changgou.goods.pojo.Spec;
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
public class SpecServiceImpl implements SpecService {
    @Autowired(required = false)
    private SpecMapper specMapper;


    /**
     * 条件+分页查询
     * @param spec
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spec> findPage(Spec spec, int page, int size) {
        //开始分页
        PageHelper.startPage(size,page);
        //构建查询对象
        Example example = createExample(spec);
        //按条件查询spec
        List<Spec> specs = specMapper.selectByExample(example);
        return new PageInfo<Spec>(specs);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spec> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        List<Spec> specs = specMapper.selectAll();
        return new PageInfo<>(specs);
    }

    /**
     * 多条件查询
     * @param spec
     * @return
     */
    @Override
    public List<Spec> findList(Spec spec) {
        Example example = createExample(spec);
        List<Spec> specs = specMapper.selectByExample(example);
        return specs;
    }



    /**
     * 删除spec
     * @param id
     */
    @Override
    public void delete(Integer id) {
        specMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改spec
     * @param spec
     */
    @Override
    public void update(Spec spec) {
        specMapper.updateByPrimaryKey(spec);
    }

    /**
     * 添加spec
     * @param spec
     */
    @Override
    public void add(Spec spec) {
        specMapper.insertSelective(spec);
    }

    /**
     * 根据id查询spec
     * @param id
     * @return
     */
    @Override
    public Spec findById(Integer id) {
        return specMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有spec
     * @return
     */
    @Override
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }


    /**
     * 构建查询对象
     * @param spec
     * @return
     */
    private Example createExample(Spec spec) {
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if (spec!=null) {
            if (!StringUtils.isEmpty(spec.getName())) {
                criteria.andLike("name","%"+spec.getName()+"%");
            }
            if (!StringUtils.isEmpty(spec.getOptions())) {
                criteria.andLike("options","%"+spec.getOptions()+"%");
            }
            if (!StringUtils.isEmpty(spec.getSeq())) {
                criteria.andLike("seq","%"+spec.getSeq()+"%");
            }
            if (!StringUtils.isEmpty(spec.getTemplateId())) {
                criteria.andLike("templateId","%"+spec.getTemplateId()+"%");
            }
        }
        return example;
    }
}
