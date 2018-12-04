package com.lucky.myblog.service;

import com.lucky.myblog.model.bo.ArchiveBo;

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

}
