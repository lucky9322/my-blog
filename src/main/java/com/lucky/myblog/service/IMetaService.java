package com.lucky.myblog.service;

import com.lucky.myblog.dto.MetaDto;
import com.lucky.myblog.model.vo.MetaVo;

import java.util.List;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/5.
 */
public interface IMetaService {
    /**
     * 根据类型查询项目列表
     * @param types
     * @return
     */
    List<MetaVo> getMetas(String types);

    /**
     * 保存多个项目
     * @param cid
     * @param names
     * @param type
     */
    void saveMetas(Integer cid, String names, String type);

    /**
     * 根据类型查询项目列表，带项目下面的文章数
     * @return
     */
    List<MetaDto> getMetaList(String type, String orderby, int limit);

    /**
     * 保存项目
     * @param type
     * @param name
     * @param mid
     */
    void saveMeta(String type, String name, Integer mid);

    /**
     * 删除项目
     * @param mid
     */
    void delete(int mid);

    /**
     * 更新项目
     * @param metas
     */
    void update(MetaVo metas);

    /**
     * 保存项目
     * @param metas
     */
    void saveMeta(MetaVo metas);

}
