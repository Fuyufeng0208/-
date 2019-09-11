package com.changgou.goods.controller;

import com.changgou.goods.service.SpecService;
import com.changgou.goods.pojo.Spec;
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
@RequestMapping("/spec")
@CrossOrigin
public class SpecController {
    @Autowired
    private SpecService specService;

    /**
     * 查询所有spec成功
     * @return
     */
    @GetMapping
    public Result<Spec> findAll(){
        List<Spec> list = specService.findAll();
        return new Result<>(true, StatusCode.OK,"查询所有spec成功",list);
    }

    /**
     * 添加spec成功
     * @param spec
     * @return
     */
    @PostMapping
    public Result<Spec>add(@RequestBody Spec spec){
        specService.add(spec);
        return new Result<>(true, StatusCode.OK,"添加spec成功");
    }

    /**
     * 删除spec
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Spec>delete(@PathVariable(value = "id") Integer id){
        specService.delete(id);
        return new Result<>(true, StatusCode.OK,"删除spec成功");
    }

    /**
     * 修改spec
     * @param spec
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Spec>update(@RequestBody Spec spec,@PathVariable(value = "id") Integer id){
        spec.setId(id);
        specService.update(spec);
        return new Result<>(true, StatusCode.OK,"修改spec成功");
    }

    /**
     * 根据id查询spec
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spec>findById(@PathVariable(value = "id") Integer id){
        Spec spec = specService.findById(id);
        return new Result<>(true, StatusCode.OK,"根据id查询spec成功",spec);
    }

    /**
     * 根据条件查询spec
     * @param spec
     * @return
     */
    @PostMapping("/search")
    public Result<Spec> search(@RequestBody Spec spec){
        List<Spec> list = specService.findList(spec);
        return new Result<>(true, StatusCode.OK,"根据条件查询spec成功",list);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<Spec>findPage(@PathVariable(value = "page") int page,@PathVariable(value = "size") int size){
        PageInfo<Spec> pageInfo = specService.findPage(page, size);
        return new Result<>(true,StatusCode.OK,"分页查询spec成功",pageInfo);
    }

    /**
     * 条件+分页查询
     * @param spec
     * @param size
     * @param page
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result findPage(@RequestBody Spec spec,@PathVariable(value = "size") int size,@PathVariable(value = "page") int page){
        PageInfo<Spec> pageInfo = specService.findPage(spec,size, page);
        return new Result<>(true, StatusCode.OK,"条件分页查询spec成功",pageInfo);
    }
}
