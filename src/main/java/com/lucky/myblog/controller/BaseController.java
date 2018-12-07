package com.lucky.myblog.controller;

import com.lucky.myblog.model.vo.UserVo;
import com.lucky.myblog.util.MapCache;
import com.lucky.myblog.util.TaleUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/11/28.
 */
public abstract class BaseController {

    public static String THEME = "themes/default";

    protected MapCache cache = MapCache.single();

    /**
     * @param viewName
     * @return
     */
    public String render(String viewName) {
        System.out.println(THEME+"/"+viewName);
        return THEME + "/" + viewName;
    }

    public BaseController title(HttpServletRequest servletRequest, String title) {
        servletRequest.setAttribute("title", title);
        return this;
    }

    public String render_404() {
        return "comm/error_404";
    }

    /**
     * 获取请求绑定的登录对象
     * @param request
     * @return
     */
    public UserVo user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    public Integer getUid(HttpServletRequest request){
        return this.user(request).getUid();
    }
}
