package com.lucky.myblog.service.impl;

import com.lucky.myblog.dao.MetaVoMapper;
import com.lucky.myblog.exception.TipException;
import com.lucky.myblog.model.vo.MetaVo;
import com.lucky.myblog.model.vo.MetaVoExample;
import com.lucky.myblog.model.vo.RelationshipVoKey;
import com.lucky.myblog.service.IMetaService;
import com.lucky.myblog.service.IRelationshipService;
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

    @Resource
    private IRelationshipService relationshipService;


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

    @Override
    public void saveMetas(Integer cid, String names, String type) {
        if (null == cid) {
            throw new TipException("项目关联id不能为空");
        }
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    private void saveOrUpdate(Integer cid, String name, String type) {
        MetaVoExample metaVoExample = new MetaVoExample();
        metaVoExample.createCriteria().andTypeEqualTo(type).andNameEqualTo(name);
        List<MetaVo> metaVos = metaDao.selectByExample(metaVoExample);

        int mid;
        MetaVo metas;
        if (metaVos.size() == 1) {
            metas = metaVos.get(0);
            mid = metas.getMid();
        } else if (metaVos.size() > 1) {
            throw new TipException("查询到多条数据");
        } else {
            metas = new MetaVo();
            metas.setSlug(name);
            metas.setName(name);
            metas.setType(type);
            metaDao.insertSelective(metas);
            mid = metas.getMid();
        }
        if (mid != 0) {
            Long count = relationshipService.countById(cid, mid);
            if (count == 0) {
                RelationshipVoKey relationships = new RelationshipVoKey();
                relationships.setCid(cid);
                relationships.setMid(mid);
                relationshipService.insertVo(relationships);
            }
        }
    }

}
