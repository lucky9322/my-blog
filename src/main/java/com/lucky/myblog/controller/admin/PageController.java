package com.lucky.myblog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.lucky.myblog.constant.WebConst;
import com.lucky.myblog.controller.BaseController;
import com.lucky.myblog.dto.Types;
import com.lucky.myblog.model.vo.ContentVo;
import com.lucky.myblog.model.vo.ContentVoExample;
import com.lucky.myblog.service.IContentService;
import com.lucky.myblog.service.ILogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/10.
 */
@Controller
@RequestMapping("admin/page")
public class PageController extends BaseController {


    private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);
    @Resource
    private IContentService contentsService;

    @Resource
    private ILogService logService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("created desc");
        contentVoExample.createCriteria().andTypeEqualTo(Types.PAGE.getType());
        PageInfo<ContentVo> contentsPaginator = contentsService
                .getArticlesWithpage(contentVoExample, 1, WebConst.MAX_POSTS);
        request.setAttribute("articles", contentsPaginator);
        return "admin/page_list";
    }
}
