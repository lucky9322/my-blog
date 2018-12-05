package com.lucky.myblog.service;

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

}
