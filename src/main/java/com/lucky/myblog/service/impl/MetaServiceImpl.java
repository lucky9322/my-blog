package com.lucky.myblog.service.impl;

import com.lucky.myblog.dao.MetaVoMapper;
import com.lucky.myblog.model.vo.MetaVo;
import com.lucky.myblog.model.vo.MetaVoExample;
import com.lucky.myblog.service.IMetaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/5.
 */
@Service
public class MetaServiceImpl implements IMetaService {

    @Resource
    private MetaVoMapper metaDao;

    @Override
    public List<MetaVo> getMetas(String types) {
        if (StringUtils.isNotBlank(types)) {
            MetaVoExample example = new MetaVoExample();
            example.createCriteria().andTypeEqualTo(types);
            example.setOrderByClause("sort desc, mid desc");
            return metaDao.selectByExample(example);
        }
        return null;
    }
}
