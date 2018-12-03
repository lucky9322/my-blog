package com.lucky.myblog.controller;

import javax.servlet.http.HttpServletRequest;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/11/28.
 */
public abstract class BaseController {

    public static String THEME = "themes/default";

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
}
