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

}
