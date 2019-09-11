package com.changgou.goods.controller;

import com.changgou.goods.service.BrandService;
import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by fyf on 2019/8/16
 */
@RestController
@RequestMapping("/brand")
@CrossOrigin
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌
     * @return
     */
    @GetMapping
    public Result<Brand> findAll(){
        List<Brand> brandList = brandService.findAll();
        return new Result<Brand>(true, StatusCode.OK,"查询成功",brandList);
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable(value = "id") Integer id){
        Brand brand = brandService.findById(id);
        return new Result<Brand>(true,StatusCode.OK,"查询成功",brand);
    }

    /**
     * 添加品牌
     * @param brand
     * @return
     */
    @PutMapping
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result(true,StatusCode.OK,"添加品牌成功");
    }
    /**
     * 修改品牌
     * @param id
     * @param brand
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Integer id,@RequestBody Brand brand){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"修改品牌成功");
    }

    /**
     * 删除品牌
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 品牌列表条件查询
     * @param brand
     * @return
     */
    @PostMapping(value = "/search")
    public Result<List<Brand>>findList(@RequestBody Brand brand){
        List<Brand> list = brandService.findList(brand);
        return new Result(true,StatusCode.OK,"条件查询成功",list);
    }

    /**
     * 品牌列表分页查询
     * @param size
     * @param page
     * @return
     */
    @GetMapping(value = "/search/{size}/{page}")
    public Result<PageInfo> findPage(@PathVariable(value = "size")Integer size, @PathVariable(value = "page")Integer page){
      PageInfo<Brand> pageInfo = brandService.findPage(size,page);
      return new Result<PageInfo>(true,StatusCode.OK,"品牌列表分页查询成功",pageInfo);
    }

    /**
     * 品牌列表条件+分页查询
     * @param brand
     * @param size
     * @param page
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@PathVariable(value="page")Integer page,
                                            @PathVariable(value="size") Integer size,
                                            @RequestBody  Brand brand)
    {
        PageInfo<Brand> info = brandService.findPage(brand,size,page);
        return new Result<PageInfo<Brand>>(true,StatusCode.OK,"品牌列表分页查询成功",info);
    }

    /**
     * 查询品牌列表成功
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Brand>> findBrandByCategory(@PathVariable(name="id") Integer id){
        List<Brand> brandList = brandService.findByCategory(id);
        return new Result<List<Brand>>(true,StatusCode.OK,"查询品牌列表成功",brandList);
    }
}
