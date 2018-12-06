package com.lucky.myblog.service;

import com.lucky.myblog.model.bo.ArchiveBo;
import com.lucky.myblog.model.bo.StatisticsBo;
import com.lucky.myblog.model.vo.CommentVo;
import com.lucky.myblog.model.vo.ContentVo;

import java.util.List;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/4.
 * <p>
 * 站点服务
 */
public interface ISiteService {

    /**
     * 查询文章归档
     *
     * @return
     */
    List<ArchiveBo> getArchives();

    /**
     * 最新收到的评论
     *
     * @param limit
     * @return
     */
    List<CommentVo> recentComments(int limit);

    /**
     * 最新发表的文章
     *
     * @param limit
     * @return
     */
    List<ContentVo> recentContents(int limit);

    /**
     * 获取后台统计数据
     *
     * @return
     */
    StatisticsBo getStatistics();

}
