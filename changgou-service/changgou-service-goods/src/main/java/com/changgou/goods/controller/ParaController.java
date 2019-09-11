package com.changgou.goods.controller;

import com.changgou.goods.service.ParaService;
import com.changgou.goods.pojo.Para;
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
@RequestMapping("/para")
@CrossOrigin
public class ParaController {
    @Autowired
    private ParaService paraService;

    /**
     * 查询所有para成功
     * @return
     */
    @GetMapping
    public Result<Para> findAll(){
        List<Para> list = paraService.findAll();
        return new Result<>(true, StatusCode.OK,"查询所有para成功",list);
    }

    /**
     * 添加para
     * @param para
     * @return
     */
    @PostMapping
    public Result<Para>add(@RequestBody Para para){
        paraService.add(para);
        return new Result<>(true, StatusCode.OK,"添加para成功");
    }

    /**
     * 删除para
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Para>delete(@PathVariable(value = "id") Integer id){
        paraService.delete(id);
        return new Result<>(true, StatusCode.OK,"删除para成功");
    }

    /**
     * 修改spec
     * @param para
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Para>update(@RequestBody Para para,@PathVariable(value = "id") Integer id){
        para.setId(id);
        paraService.update(para);
        return new Result<>(true, StatusCode.OK,"修改para成功");
    }

    /**
     * 根据id查询para
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spec>findById(@PathVariable(value = "id") Integer id){
        Para para = paraService.findById(id);
        return new Result<>(true, StatusCode.OK,"根据id查询para成功",para);
    }

    /**
     * 根据条件查询para
     * @param para
     * @return
     */
    @PostMapping("/search")
    public Result<Para> search(@RequestBody Para para){
        List<Para> list = paraService.findList(para);
        return new Result<>(true, StatusCode.OK,"根据条件查询para成功",list);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<Para>findPage(@PathVariable(value = "page") int page,@PathVariable(value = "size") int size){
        PageInfo<Para> pageInfo = paraService.findPage(page, size);
        return new Result<>(true,StatusCode.OK,"分页查询para成功",pageInfo);
    }

    /**
     * 条件+分页查询
     * @param para
     * @param size
     * @param page
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result findPage(@RequestBody Para para,@PathVariable(value = "size") int size,@PathVariable(value = "page") int page){
        PageInfo<Para> pageInfo = paraService.findPage(para,size, page);
        return new Result<>(true, StatusCode.OK,"条件分页查询para成功",pageInfo);
    }
}
