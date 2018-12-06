package com.lucky.myblog.service;

import com.lucky.myblog.model.vo.UserVo;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/5.
 */
public interface IUserService {

    /**
     * 通过uid查找对象
     * @param uid
     * @return
     */
    UserVo queryUserById(Integer uid);

    /**
     * 用戶登录
     * @param username
     * @param password
     * @return
     */
    UserVo login(String username, String password);

}
