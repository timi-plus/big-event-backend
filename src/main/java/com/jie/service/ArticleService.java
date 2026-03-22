package com.jie.service;

import com.jie.pojo.Article;
import com.jie.pojo.PageBean;

public interface ArticleService {

    //添加文章
    void add(Article article);


    //分页查询文章列表
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    //更新文章
    boolean update(Article article);
    //获取文章详情
    Article findById(Integer id);

    boolean delete(Integer id);
}
