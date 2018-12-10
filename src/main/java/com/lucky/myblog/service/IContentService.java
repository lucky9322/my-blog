package com.lucky.myblog.service;


import com.github.pagehelper.PageInfo;
import com.lucky.myblog.model.vo.ContentVo;
import com.lucky.myblog.model.vo.ContentVoExample;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/3.
 */
public interface IContentService {

    /**
     * 查询文章返回多条数据
     *
     * @param p     当前页
     * @param limit 每页条数
     * @return
     */
    PageInfo<ContentVo> getContents(Integer p, Integer limit);

    /**
     * 根据ID或slug获取文章
     *
     * @param id
     * @return
     */
    ContentVo getContents(String id);

    /**
     * 根据主键更新
     * @param contentVo contentVo
     */
    void updateContentByCid(ContentVo contentVo);

    /**
     * 搜索、分页
     * @param keyword keyword
     * @param page page
     * @param limit limit
     * @return ContentVo
     */
    PageInfo<ContentVo> getArticles(String keyword,Integer page,Integer limit);


    /**
     * @param commentVoExample
     * @param page
     * @param limit
     * @return
     */
    PageInfo<ContentVo> getArticlesWithpage(ContentVoExample commentVoExample, Integer page, Integer limit);


    /**
     * 发布文章
     * @param contents
     */
    String publish(ContentVo contents);

    /**
     * 根据文章id删除
     * @param cid
     */
    String deleteByCid(Integer cid);

    /**
     * 编辑文章
     * @param contents
     */
    String updateArticle(ContentVo contents);

    /**
     * 更新原有文章的category
     * @param ordinal
     * @param newCatefory
     */
    void updateCategory(String ordinal,String newCatefory);
}
