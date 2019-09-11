package com.changgou.goods.controller;

import com.changgou.goods.service.AlbumService;
import com.changgou.goods.pojo.Album;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by fyf on 2019/8/20
 */
@RestController
@RequestMapping("/album")
@CrossOrigin
public class AlbumController {
    @Autowired(required = false)
    private AlbumService albumService;

    /**
     * 查询全部albums数据
     * @return
     */
    @GetMapping
    public Result<Album> findAll(){
        List<Album> list = albumService.findAll();
        return new Result<>(true, StatusCode.OK,"查询所有album成功",list);
    }

    /**
     * 添加album数据
     * @param album
     * @return
     */
    @PostMapping
    public Result<Album> add(@RequestBody Album album){
        albumService.add(album);
        return new Result<>(true,StatusCode.OK,"添加album成功");
    }

    /**
     * 根据id查询album
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Album> findById(@PathVariable Long id){
        Album album = albumService.findById(id);
        return new Result<>(true,StatusCode.OK,"根据id查询album成功",album);
    }

    /**
     * 多条件查询album
     * @param album
     * @return
     */
    @PostMapping("/search")
    public Result<Album> findList(@RequestBody Album album){
        List<Album> list = albumService.findList(album);
        return new Result<>(true,StatusCode.OK,"条件查询成功",list);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{size}/{page}")
    public Result<Album> findPage(@PathVariable(value = "page") int page,@PathVariable(value = "size")int size){
        PageInfo<Album> page1 = albumService.findPage(page, size);
        return new Result<>(true,StatusCode.OK,"分页查询成功",page1);
    }

    /**
     * 条件+分页查询
     * @param album 查询条件
     * @param page 当前页
     * @param size 每页数据条数
     * @return
     */
    @PostMapping("/search/{size}/{page}")
    public Result<Album> findPage(@RequestBody Album album,
                             @PathVariable(value = "page") int page,
                             @PathVariable(value = "size")int size){
        PageInfo<Album> page1 = albumService.findPage(album, page, size);
        return new Result<>(true,StatusCode.OK,"分页查询成功",page1);
    }

    /**
     * 修改album数据
     * @param album
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Album> update(@RequestBody Album album,@PathVariable(value = "id") Long id){
        //设置主键
        album.setId(id);
        //修改数据
        albumService.update(album);
        return new Result<>(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除album数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Album> delete(@PathVariable(value = "id") Long id){
        albumService.delete(id);
        return new Result<>(true,StatusCode.OK,"删除成功");
    }
}
