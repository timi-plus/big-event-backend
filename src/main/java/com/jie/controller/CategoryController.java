package com.jie.controller;


import com.jie.pojo.Category;
import com.jie.pojo.Result;
import com.jie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    //添加分类
    @PostMapping
    public Result add(@RequestBody @Validated(Category.Add.class) Category category){

        categoryService.add(category);
        return Result.success();
    }
    //获取分类列表
    @GetMapping
    public Result<List< Category>> list(){
        List<Category> cs = categoryService.list();
        return Result.success(cs);
    }

    //获取文章分类详情
    @GetMapping("/detail")
    public Result<Category> detail(Integer id){
        Category cs = categoryService.findById(id);
        return Result.success(cs);
    }

    //更新分类
    @PutMapping
    public Result update(@RequestBody @Validated(Category.Update.class) Category category){
        categoryService.update(category);
        return Result.success();
    }
    //删除分类
    @DeleteMapping
    public Result delete(Integer id){
        categoryService.delete(id);
        return Result.success();
    }
}
