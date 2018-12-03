package com.lucky.myblog.controller;


import com.github.pagehelper.PageInfo;
import com.lucky.myblog.constant.WebConst;
import com.lucky.myblog.model.vo.ContentVo;
import com.lucky.myblog.service.IContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/11/28.
 */
@Controller
public class IndexController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);


    @Resource
    private IContentService contentService;

    /**
     * 首页
     *
     * @param servletRequest
     * @param limit
     * @return
     */
    @GetMapping(value = "/")
    public String index(HttpServletRequest servletRequest,
                        @RequestParam(value = "limit", defaultValue = "12") int limit) {

        return this.index(servletRequest,1,limit);
    }

    /**
     * 首页分页
     *
     * @param servletRequest
     * @param p              第几页
     * @param limit          每页大小
     * @return 主页
     */
    @GetMapping(value = "page/{p}")
    public String index(HttpServletRequest servletRequest,
                        @PathVariable int p,
                        @RequestParam(value = "limit", defaultValue = "12") int limit) {
        p = p < 0 || p > WebConst.MAX_PAGE ? 1 : p;
        PageInfo<ContentVo> articles = contentService.getContents(p, limit);
        servletRequest.setAttribute("articles", articles);
        if (p > 1) {
            this.title(servletRequest, "第" + p + "页");
        }
        return this.render("index");
    }
}
