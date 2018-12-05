package com.lucky.myblog.controller;


import com.github.pagehelper.PageInfo;
import com.lucky.myblog.constant.WebConst;
import com.lucky.myblog.dto.Types;
import com.lucky.myblog.model.bo.ArchiveBo;
import com.lucky.myblog.model.bo.CommentBo;
import com.lucky.myblog.model.vo.ContentVo;
import com.lucky.myblog.model.vo.MetaVo;
import com.lucky.myblog.service.ICommentService;
import com.lucky.myblog.service.IContentService;
import com.lucky.myblog.service.IMetaService;
import com.lucky.myblog.service.ISiteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/11/28.
 */
@Controller
public class IndexController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);


    @Resource
    private IContentService contentService;

    @Resource
    private ICommentService commentService;

    @Resource
    private ISiteService siteService;

    @Resource
    private IMetaService metaService;

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

        return this.index(servletRequest, 1, limit);
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

    @GetMapping(value = {"/article/{cid}", "/article/{cid}.html"})
    public String getArticle(HttpServletRequest servletRequest, @PathVariable String cid) {
        ContentVo contents = contentService.getContents(cid);
        if (null == contents) {
            return this.render_404();
        }
        servletRequest.setAttribute("article", contents);
        servletRequest.setAttribute("is_post", true);
        completeArticle(servletRequest, contents);
        return this.render("post");
    }


    /**
     * 抽取公共方法
     *
     * @param request
     * @param contents
     */
    private void completeArticle(HttpServletRequest request, ContentVo contents) {
        if (contents.getAllowComment()) {
            String cp = request.getParameter("cp");
            if (StringUtils.isBlank(cp)) {
                cp = "1";
            }
            request.setAttribute("cp", cp);
            PageInfo<CommentBo> commentsPaginator = commentService.getComments(contents.getCid(), Integer.parseInt(cp), 6);
            request.setAttribute("comments", commentsPaginator);
        }
    }

    /**
     * 归档页
     *
     * @return
     */
    @GetMapping(value = "archives")
    public String archives(HttpServletRequest request) {
        List<ArchiveBo> archives = siteService.getArchives();
        request.setAttribute("archives", archives);
        return this.render("archives");
    }

    /**
     * 友链
     *
     * @param servletRequest
     * @return
     */
    @GetMapping(value = "links")
    public String links(HttpServletRequest servletRequest) {
        List<MetaVo> links = metaService.getMetas(Types.LINK.getType());
        servletRequest.setAttribute("links", links);
        return this.render("links");
    }
}
