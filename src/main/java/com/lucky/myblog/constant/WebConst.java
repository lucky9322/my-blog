package com.lucky.myblog.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/3.
 */
public class WebConst {
    /**
     * 最大页码
     */
    public static final int MAX_PAGE = 100;

    public static Map<String, String> initConfig = new HashMap<>();

    /**
     * 同一篇文章在2个小时内无论点击多少次只算一次阅读
     */
    public static Integer HITS_LIMIT_TIME = 7200;

    /**
     * 点击次数超过多少更新到数据库
     */
    public static final int HIT_EXCEED = 10;

    /**
     * 成功返回
     */
    public static String SUCCESS_RESULT = "SUCCESS";

    public static String LOGIN_SESSION_KEY = "login_user";

    public static final String USER_IN_COOKIE = "S_L_ID";

    /**
     * aes加密加盐
     */
    public static String AES_SALT = "0123456789abcdef";

    /**
     * 最大获取文章条数
     */
    public static final int MAX_POSTS = 9999;


    /**
     * 文章标题最多可以输入的文字个数
     */
    public static final int MAX_TITLE_COUNT = 200;

    /**
     * 文章最多可以输入的文字数
     */
    public static final int MAX_TEXT_COUNT = 200000;

}
