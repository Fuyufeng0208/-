package com.changgou.goods.controller;

import com.changgou.goods.service.CateGoryService;
import com.changgou.goods.pojo.Category;
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
@RequestMapping("/category")
@CrossOrigin
public class CateGoryController {
    @Autowired
    private CateGoryService cateGoryService;

    /**
     * 查询所有Category
     * @return
     */
    @GetMapping
    public Result<Category> findAll(){
        List<Category> list = cateGoryService.findAll();
        return new Result<>(true, StatusCode.OK,"查询所有category成功",list);
    }

    /**
     * 添加Category
     * @param category
     * @return
     */
    @PostMapping
    public Result<Category> add(@RequestBody Category category){
        cateGoryService.add(category);
        return new Result<>(true, StatusCode.OK,"添加category成功");
    }

    /**
     * 根据父节点ID查询所有Category子节点
     * @return
     */
    @GetMapping("/list/{pid}")
    public Result<Category>findByParentId(@PathVariable(value = "pid") Integer pid){
        List<Category> byParentId = cateGoryService.findByParentId(pid);
        return new Result<>(true, StatusCode.OK,"根据父节点ID查询所有category成功",byParentId);
    }

    /**
     * 条件查询Category
     * @param category
     * @return
     */
    @PostMapping("/search")
    public Result findList(@RequestBody Category category){
        List<Category> list = cateGoryService.findList(category);
        return new Result<>(true, StatusCode.OK,"条件查询category成功",list);
    }

    /**
     * 分页查询Category
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<Category> findPage(@PathVariable(value = "page") int page,@PathVariable(value = "size") int size){
        PageInfo<Category> pageInfo = cateGoryService.findPage(page, size);
        return new Result<>(true, StatusCode.OK,"分页查询category成功",pageInfo);
    }

    /**
     * 条件分页查询category
     * @param category
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<Category> findPage(@RequestBody Category category ,@PathVariable(value = "page") int page,@PathVariable(value = "size") int size){
        PageInfo<Category> pageInfo = cateGoryService.findPage(category,page, size);
        return new Result<>(true, StatusCode.OK,"条件分页查询category成功",pageInfo);
    }

    /**
     * 根据id查询category
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Category>findById(@PathVariable(value = "id") Integer id){
        Category category = cateGoryService.findById(id);
        return new Result<>(true, StatusCode.OK,"根据id查询category成功",category);
    }

    /**
     * 根据ID修改Category
     * @param category
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Category>update(@RequestBody Category category,@PathVariable(value = "id")Integer id){
        category.setId(id);
        cateGoryService.update(category);
        return new Result<>(true, StatusCode.OK,"根据ID修改Category成功");
    }

    /**
     * 根据ID删除Category
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Category> delete(@PathVariable(value = "id") Integer id){
        cateGoryService.delete(id);
        return new Result<>(true, StatusCode.OK,"根据ID删除Category成功");
    }
}
