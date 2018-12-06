package com.lucky.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.lucky.myblog.dao.AttachVoMapper;
import com.lucky.myblog.dao.CommentVoMapper;
import com.lucky.myblog.dao.ContentVoMapper;
import com.lucky.myblog.dao.MetaVoMapper;
import com.lucky.myblog.dto.Types;
import com.lucky.myblog.model.bo.ArchiveBo;
import com.lucky.myblog.model.bo.StatisticsBo;
import com.lucky.myblog.model.vo.*;
import com.lucky.myblog.service.ISiteService;
import com.lucky.myblog.util.DateKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/4.
 */
@Service
public class SiteServiceImpl implements ISiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Resource
    private ContentVoMapper contentDao;

    @Resource
    private CommentVoMapper commentDao;

    @Resource
    private AttachVoMapper attachDao;
    @Resource
    private MetaVoMapper metaDao;


    @Override
    public List<ArchiveBo> getArchives() {
        LOGGER.debug("Enter getArchives method");
        List<ArchiveBo> archives = contentDao.findReturnArchiveBo();
        if (null != archives) {
            archives.forEach(archive -> {
                ContentVoExample example = new ContentVoExample();
                ContentVoExample.Criteria criteria = example.createCriteria()
                        .andTypeEqualTo(Types.ARTICLE.getType())
                        .andStatusEqualTo(Types.PUBLISH.getType());
                example.setOrderByClause("created desc");
                String date = archive.getDate();
                Date sd = DateKit.dateFormat(date, "yyyy年MM月");
                int start = DateKit.getUnixTimeByDate(sd);
                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;
                criteria.andCreatedGreaterThan(start);
                criteria.andCreatedLessThan(end);
                List<ContentVo> contentVos = contentDao.selectByExample(example);
                archive.setArticles(contentVos);
            });
        }
        return archives;
    }

    @Override
    public List<CommentVo> recentComments(int limit) {
        LOGGER.debug("Enter recentComments method:limit={}", limit);
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        CommentVoExample example = new CommentVoExample();
        example.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        List<CommentVo> byPage = commentDao.selectByExampleWithBLOBs(example);
        LOGGER.debug("Exit recentComments method");
        return byPage;
    }

    @Override
    public List<ContentVo> recentContents(int limit) {
        LOGGER.debug("Enter recentContents method");
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        ContentVoExample example = new ContentVoExample();
        example.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        List<ContentVo> contentVos = contentDao.selectByExampleWithBLOBs(example);
        LOGGER.debug("Exit recentContents method");
        return contentVos;
    }

    @Override
    public StatisticsBo getStatistics() {
        LOGGER.debug("Enter getStatistics method");
        StatisticsBo statistics = new StatisticsBo();

        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        Integer articles =   contentDao.countByExample(contentVoExample);

        Integer comments = commentDao.countByExample(new CommentVoExample());

        Integer attachs = attachDao.countByExample(new AttachVoExample());

        MetaVoExample metaVoExample = new MetaVoExample();
        metaVoExample.createCriteria().andTypeEqualTo(Types.LINK.getType());
        Integer links = metaDao.countByExample(metaVoExample);

        statistics.setArticles(articles);
        statistics.setComments(comments);
        statistics.setAttachs(attachs);
        statistics.setLinks(links);

        LOGGER.debug("Exit getStatistics method");
        return statistics;
    }
}
