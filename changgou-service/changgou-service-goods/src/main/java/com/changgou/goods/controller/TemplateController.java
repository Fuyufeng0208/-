package com.changgou.goods.controller;

import com.changgou.goods.service.TemplateService;
import com.changgou.goods.pojo.Template;
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
@RequestMapping("/template")
@CrossOrigin
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     * 查询所有Template
     * @return
     */
    @GetMapping
    public Result<Template> findAll(){
        List<Template> all = templateService.findAll();
        return new Result<>(true, StatusCode.OK,"查询所有template成功",all);
    }

    /**
     * 添加template
     * @param template
     * @return
     */
    @PostMapping
    public Result<Template>add(@RequestBody Template template){
        templateService.add(template);
        return new Result<>(true, StatusCode.OK,"添加template成功");
    }

    /**
     * 删除template
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Template>delete(@PathVariable(value = "id") Integer id){
        templateService.delete(id);
        return new Result<>(true, StatusCode.OK,"删除template成功");
    }

    /**
     * 修改template
     * @param template
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Template>update(@RequestBody Template template,@PathVariable(value = "id") Integer id){
        template.setId(id);
        templateService.update(template);
        return new Result<>(true, StatusCode.OK,"修改template成功");
    }

    /**
     * 根据id查询template
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Template>findById(@PathVariable(value = "id") Integer id){
        Template template = templateService.findById(id);
        return new Result<>(true, StatusCode.OK,"根据id查询template成功",template);
    }

    /**
     * 根据条件查询template
     * @param template
     * @return
     */
    @PostMapping("/search")
    public Result<Template> search(@RequestBody Template template){
        List<Template> list = templateService.findList(template);
        return new Result<>(true, StatusCode.OK,"根据条件查询template成功",list);
    }

    /**
     * 分页查询
     * @param size
     * @param page
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result findPage(@PathVariable(value = "size") int size,@PathVariable(value = "page") int page){
        PageInfo<Template> pageInfo = templateService.findPage(size, page);
        return new Result<>(true, StatusCode.OK,"分页查询template成功",pageInfo);
    }

    /**
     * 条件+分页查询
     * @param template 查询条件
     * @param size 每页数据条数
     * @param page 当前页
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result findPage(@RequestBody Template template,@PathVariable(value = "size") int size,@PathVariable(value = "page") int page){
        PageInfo<Template> pageInfo = templateService.findPage(template,size, page);
        return new Result<>(true, StatusCode.OK,"条件分页查询template成功",pageInfo);
    }
}
