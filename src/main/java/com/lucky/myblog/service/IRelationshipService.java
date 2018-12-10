package com.lucky.myblog.service;

import com.lucky.myblog.model.vo.RelationshipVoKey;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/7.
 */
public interface IRelationshipService {
    /**
     * 按主键统计条数
     * @param cid
     * @param mid
     * @return 条数
     */
    Long countById(Integer cid, Integer mid);

    /**
     * 保存對象
     * @param relationshipVoKey
     */
    void insertVo(RelationshipVoKey relationshipVoKey);

    /**
     * 按住键删除
     * @param cid
     * @param mid
     */
    void deleteById(Integer cid, Integer mid);

}
