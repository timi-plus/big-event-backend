package com.jie.controller;


import com.jie.pojo.Article;
import com.jie.pojo.PageBean;
import com.jie.pojo.Result;
import com.jie.service.ArticleService;
import com.jie.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    //添加文章
    @PostMapping
    public Result add(@RequestBody @Validated Article article){
        articleService.add(article);
        return Result.success();
    }
    //分页查询文章列表
    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam (required = false) Integer categoryId,
            @RequestParam (required = false)String state
    ){
        PageBean<Article> pageBean = articleService.list(pageNum,pageSize,categoryId,state);
        return Result.success(pageBean);
    }
    //获取文章详情
    @GetMapping("/detail")
    public Result<Article> detail(Integer id){
        Article article = articleService.findById(id);
        return Result.success(article);
    }
    //更新文章
    @PutMapping
    public Result update(@RequestBody @Validated Article article){

        if(!articleService.update(article)){
            return Result.error("没有权限");
        }
        return Result.success();
    }
    //删除文章
    @DeleteMapping
    public Result delete(Integer id){
        if(!articleService.delete(id)){
            return Result.error("没有权限");
        }
        return Result.success();
    }

}
