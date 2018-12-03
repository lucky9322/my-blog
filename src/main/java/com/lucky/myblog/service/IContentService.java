package com.lucky.myblog.service;


import com.github.pagehelper.PageInfo;
import com.lucky.myblog.model.vo.ContentVo;

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
}
