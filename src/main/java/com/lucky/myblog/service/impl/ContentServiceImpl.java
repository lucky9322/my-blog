package com.lucky.myblog.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.myblog.dao.ContentVoMapper;
import com.lucky.myblog.dto.Types;
import com.lucky.myblog.exception.TipException;
import com.lucky.myblog.model.vo.ContentVo;
import com.lucky.myblog.model.vo.ContentVoExample;
import com.lucky.myblog.service.IContentService;
import com.lucky.myblog.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/3.
 */
@Service
public class ContentServiceImpl implements IContentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Resource
    private ContentVoMapper contentDao;

    @Override
    public PageInfo<ContentVo> getContents(Integer p, Integer limit) {
        LOGGER.debug("Enter getContents method");
        ContentVoExample example = new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType())
                .andStatusEqualTo(Types.PUBLISH.getType());
        PageHelper.startPage(p, limit);
        List<ContentVo> data = contentDao.selectByExampleWithBLOBs(example);
        PageInfo<ContentVo> pageInfo = new PageInfo<>(data);
        LOGGER.debug("Exit getContents method");
        return pageInfo;
    }

    @Override
    public ContentVo getContents(String id) {
        if (StringUtils.isNotBlank(id)) {
            if (Tools.isNumber(id)) {
                ContentVo contentVo = contentDao.selectByPrimaryKey(Integer.valueOf(id));
                return contentVo;
            } else {
                ContentVoExample example = new ContentVoExample();
                example.createCriteria().andSlugEqualTo(id);
                List<ContentVo> contentVos = contentDao.selectByExampleWithBLOBs(example);
                if (contentVos.size() != 1) {
                    throw new TipException("query content by id and return is not one");
                }
                return contentVos.get(0);
            }
        }
        return null;
    }

    @Override
    public void updateContentByCid(ContentVo contentVo) {
        if (null != contentVo && null != contentVo.getCid()) {
            contentDao.updateByPrimaryKeySelective(contentVo);
        }
    }

    @Override
    public PageInfo<ContentVo> getArticles(String keyword, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        ContentVoExample contentVoExample = new ContentVoExample();
        ContentVoExample.Criteria criteria = contentVoExample.createCriteria();
        criteria.andTypeEqualTo(Types.ARTICLE.getType());
        criteria.andStatusEqualTo(Types.PUBLISH.getType());
        criteria.andTitleLike("%" + keyword + "%");
        contentVoExample.setOrderByClause("created desc");
        List<ContentVo> contentVos = contentDao.selectByExampleWithBLOBs(contentVoExample);
        return new PageInfo<>(contentVos);
    }

    @Override
    public PageInfo<ContentVo> getArticlesWithpage(ContentVoExample commentVoExample,
                                                   Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<ContentVo> contentVos = contentDao.selectByExampleWithBLOBs(commentVoExample);
        return new PageInfo<>(contentVos);
    }
}
