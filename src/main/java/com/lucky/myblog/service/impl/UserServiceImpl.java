package com.lucky.myblog.service.impl;

import com.lucky.myblog.dao.UserVoMapper;
import com.lucky.myblog.model.vo.UserVo;
import com.lucky.myblog.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/5.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserVoMapper userDao;

    @Override
    public UserVo queryUserById(Integer uid) {
        UserVo userVo = null;
        if (uid != null) {
            userVo = userDao.selectByPrimaryKey(uid);
        }
        return userVo;
    }
}
