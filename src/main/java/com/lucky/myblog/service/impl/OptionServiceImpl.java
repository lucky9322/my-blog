package com.lucky.myblog.service.impl;

import com.lucky.myblog.dao.OptionVoMapper;
import com.lucky.myblog.model.vo.OptionVo;
import com.lucky.myblog.model.vo.OptionVoExample;
import com.lucky.myblog.service.IOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/10.
 */
@Service
public class OptionServiceImpl implements IOptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OptionServiceImpl.class);


    @Resource
    private OptionVoMapper optionDao;


    @Override
    public List<OptionVo> getOptions() {
        return optionDao.selectByExample(new OptionVoExample());

    }

    @Override
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::insertOption);
        }
    }

    @Override
    @Transactional
    public void insertOption(String name, String value) {
        LOGGER.debug("Enter insertOption method:name={},value={}", name, value);
        OptionVo optionVo = new OptionVo();
        optionVo.setName(name);
        optionVo.setValue(value);
        if (optionDao.selectByPrimaryKey(name) == null) {
            optionDao.insertSelective(optionVo);
        } else {
            optionDao.updateByPrimaryKeySelective(optionVo);
        }
        LOGGER.debug("Exit insertOption method.");
    }
}
