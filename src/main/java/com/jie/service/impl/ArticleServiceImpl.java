package com.jie.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jie.mapper.ArticleMapper;
import com.jie.pojo.Article;
import com.jie.pojo.PageBean;
import com.jie.pojo.Result;
import com.jie.service.ArticleService;
import com.jie.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    //添加文章
    @Override
    public void add(Article article) {
        //补充信息
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        article.setCreateUser(userId);
        articleMapper.add( article);
    }

    //查询文章列表
    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //new PageBean
        PageBean<Article> pageBean = new PageBean<>();
        //pagehelper
        PageHelper.startPage(pageNum,pageSize);

        //调用mapper
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        List<Article> articles = articleMapper.list(userId,categoryId,state);
        //page中提供了方法,可以获得pagehepler中获取的分页数据
        Page<Article> page = (Page<Article>) articles;

        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());

        return pageBean;
    }

    @Override
    public boolean update(Article article) {
        // 验证文章 ID 是否有效
        if (article.getId() == null) {
            return false;
        }
        
        // 获取当前登录用户 ID
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer userid = (Integer) claims.get("id");
        if (userid == null) {
            return false;
        }
        
        // 查询数据库中的文章信息
        Article dbArticle = articleMapper.findById(article.getId());
        if (dbArticle == null) {
            // 文章不存在
            return false;
        }
        
        // 验证权限：只有文章创建者才能修改
        if (!dbArticle.getCreateUser().equals(userid)) {
            return false;
        }
        
        // 补充更新时间并执行更新
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
        return true;
    }

    @Override
    public Article findById(Integer id) {
        Article article = articleMapper.findById(id);
        return article;
    }

    @Override
    public boolean delete(Integer id) {
        // 获取当前登录用户 ID
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer userid = (Integer) claims.get("id");

        // 查询数据库中的文章信息
        Article dbArticle = articleMapper.findById(id);
        if (dbArticle == null) {
            // 文章不存在
            return false;
        }
        if (!dbArticle.getCreateUser().equals(userid)) {
            // 非作者不能删除
            return false;
        }
        articleMapper.delete(id);
        return true;
    }
}
