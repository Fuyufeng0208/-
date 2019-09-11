package com.changgou.goods.service.impl;

import com.changgou.goods.dao.ParaMapper;
import com.changgou.goods.service.ParaService;
import com.changgou.goods.pojo.Para;
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
public class ParaServiceImpl implements ParaService {

    @Autowired(required = false)
    private ParaMapper paraMapper;

    /**
     * 条件+分页查询
     * @param para
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Para> findPage(Para para, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(para);
        List<Para> paras = paraMapper.selectByExample(example);
        return new PageInfo<>(paras);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Para> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        List<Para> paras = paraMapper.selectAll();
        return new PageInfo<>(paras);
    }

    /**
     * 条件查询
     * @param para
     * @return
     */
    @Override
    public List<Para> findList(Para para) {
        Example example = createExample(para);
        List<Para> paras = paraMapper.selectByExample(example);
        return paras;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id) {
        paraMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改
     * @param para
     */
    @Override
    public void update(Para para) {
        paraMapper.updateByPrimaryKey(para);
    }

    /**
     * 添加
     * @param para
     */
    @Override
    public void add(Para para) {
        paraMapper.insertSelective(para);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Para findById(Integer id) {
        return paraMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Para> findAll() {
        return paraMapper.selectAll();
    }

    /**
     * Para构建查询对象
     * @param para
     * @return
     */
    public Example createExample(Para para){
        Example example=new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        if(para!=null){
            // id
            if(!StringUtils.isEmpty(para.getId())){
                criteria.andEqualTo("id",para.getId());
            }
            // 名称
            if(!StringUtils.isEmpty(para.getName())){
                criteria.andLike("name","%"+para.getName()+"%");
            }
            // 选项
            if(!StringUtils.isEmpty(para.getOptions())){
                criteria.andEqualTo("options",para.getOptions());
            }
            // 排序
            if(!StringUtils.isEmpty(para.getSeq())){
                criteria.andEqualTo("seq",para.getSeq());
            }
            // 模板ID
            if(!StringUtils.isEmpty(para.getTemplateId())){
                criteria.andEqualTo("templateId",para.getTemplateId());
            }
        }
        return example;
    }
}
