package com.changgou.goods.service.impl;

import com.changgou.goods.dao.AlbumMapper;
import com.changgou.goods.service.AlbumService;
import com.changgou.goods.pojo.Album;
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
public class AlbumServiceImpl implements AlbumService {
    @Autowired(required = false)
    private AlbumMapper albumMapper;

    /**
     * 多条件分页查询
     * @param album 查询条件
     * @param page 当前页
     * @param size 每页数据条数
     * @return
     */
    @Override
    public PageInfo<Album> findPage(Album album, int page, int size) {
        //开始分页
        PageHelper.startPage(page,size);
        //按条件查询
        Example example = createExample(album);
        List<Album> albums = albumMapper.selectByExample(example);
        return new PageInfo<Album>(albums);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Album> findPage(int page, int size) {
        //开始分页
        PageHelper.startPage(size,page);
        //查询所有
        List<Album> albums = albumMapper.selectAll();
        return new PageInfo<Album>(albums);
    }

    /**
     * 多条件查询album
     * @param album
     * @return
     */
    @Override
    public List<Album> findList(Album album) {
        Example example = createExample(album);
        return albumMapper.selectByExample(example);
    }

    /**
     * 删除album数据
     * @param id
     */
    @Override
    public void delete(Long id) {
        albumMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改album数据
     * @param album
     */
    @Override
    public void update(Album album) {
        albumMapper.updateByPrimaryKey(album);
    }

    /**
     * 添加album数据
     * @param album
     */
    @Override
    public void add(Album album) {
        albumMapper.insertSelective(album);
    }

    /**
     * 根据id查询album
     * @param id
     * @return
     */
    @Override
    public Album findById(Long id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有album
     * @return
     */
    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }

    /**
     * 构建查询条件
     * @param album
     */
    private Example createExample(Album album) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if (album!=null) {
            if (!StringUtils.isEmpty(album.getTitle())) {
                criteria.andLike("title","%"+album.getTitle()+"%");
            }
        }
        return example;
    }
}
