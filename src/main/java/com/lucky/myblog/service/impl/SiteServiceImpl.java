package com.lucky.myblog.service.impl;

import com.lucky.myblog.dao.ContentVoMapper;
import com.lucky.myblog.dto.Types;
import com.lucky.myblog.model.bo.ArchiveBo;
import com.lucky.myblog.model.vo.ContentVo;
import com.lucky.myblog.model.vo.ContentVoExample;
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
}
