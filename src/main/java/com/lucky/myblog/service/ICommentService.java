package com.lucky.myblog.service;

import com.github.pagehelper.PageInfo;
import com.lucky.myblog.model.bo.CommentBo;
import com.lucky.myblog.model.vo.CommentVo;
import com.lucky.myblog.model.vo.CommentVoExample;

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

    /**
     * 获取文章下的评论
     * @param commentVoExample
     * @param page
     * @param limit
     * @return CommentVo
     */
    PageInfo<CommentVo> getCommentsWithPage(CommentVoExample commentVoExample, int page, int limit);

    /**
     * 根据主键查询评论
     * @param coid
     * @return
     */
    CommentVo getCommentById(Integer coid);

    /**
     * 删除评论，暂时没用
     * @param coid
     * @param cid
     * @throws Exception
     */
    void delete(Integer coid, Integer cid);

    /**
     * 更新评论状态
     * @param comments
     */
    void update(CommentVo comments);
}
