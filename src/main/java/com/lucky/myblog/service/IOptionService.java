package com.lucky.myblog.service;

import com.lucky.myblog.model.vo.OptionVo;

import java.util.List;
import java.util.Map;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/10.
 */
public interface IOptionService {
    List<OptionVo> getOptions();

    /**
     * 保存一组配置
     *
     * @param options
     */
    void saveOptions(Map<String, String> options);

    void insertOption(String name, String value);

}
