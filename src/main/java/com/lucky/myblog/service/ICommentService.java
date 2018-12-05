package com.lucky.myblog.service;

import com.github.pagehelper.PageInfo;
import com.lucky.myblog.model.bo.CommentBo;
import com.lucky.myblog.model.vo.CommentVo;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/4.
 */
public interface ICommentService {

    /**
     * 获取文章下的评论
     *
     * @param cid
     * @param page
     * @param limit
     * @return CommentBo
     */
    PageInfo<CommentBo> getComments(Integer cid, int page, int limit);


    /**
     * 进行评论
     *
     * @param commentVo
     */
    String insertComment(CommentVo commentVo);

}
