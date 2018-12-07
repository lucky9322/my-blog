package com.lucky.myblog.controller;


import com.github.pagehelper.PageInfo;
import com.lucky.myblog.constant.WebConst;
import com.lucky.myblog.dto.ErrorCode;
import com.lucky.myblog.dto.Types;
import com.lucky.myblog.model.bo.ArchiveBo;
import com.lucky.myblog.model.bo.CommentBo;
import com.lucky.myblog.model.bo.RestResponseBo;
import com.lucky.myblog.model.vo.CommentVo;
import com.lucky.myblog.model.vo.ContentVo;
import com.lucky.myblog.model.vo.MetaVo;
import com.lucky.myblog.service.ICommentService;
import com.lucky.myblog.service.IContentService;
import com.lucky.myblog.service.IMetaService;
import com.lucky.myblog.service.ISiteService;
import com.lucky.myblog.util.IPKit;
import com.lucky.myblog.util.PatternKit;
import com.lucky.myblog.util.TaleUtils;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.net.URLEncoder;
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

    /**
     * 自定义页面，如关于页面
     *
     * @param pagename
     * @param servletRequest
     * @return
     */
    @GetMapping(value = "/{pagename}")
    public String page(@PathVariable String pagename, HttpServletRequest servletRequest) {
        ContentVo contents = contentService.getContents(pagename);
        if (null == contents) {
            return this.render_404();
        }
        if (contents.getAllowComment()) {
            String cp = servletRequest.getParameter("cp");
            if (StringUtils.isBlank(cp)) {
                cp = "1";
            }
            PageInfo<CommentBo> commentsPaginator = commentService.
                    getComments(contents.getCid(), Integer.parseInt(cp), 6);
            servletRequest.setAttribute("comments", commentsPaginator);
        }
        servletRequest.setAttribute("article", contents);
        if (!checkHitsFrequency(servletRequest, String.valueOf(contents.getCid()))) {
            updateArticleHit(contents.getCid(), contents.getHits());
        }
        return this.render("page");
    }


    /**
     * 更新文章点击率
     *
     * @param cid
     * @param chits
     */
    private void updateArticleHit(Integer cid, Integer chits) {
        Integer hits = cache.hget("article" + cid, "hits");
        if (chits == null) {
            chits = 0;
        }
        hits = null == hits ? 1 : hits + 1;
        if (hits >= WebConst.HIT_EXCEED) {
            ContentVo temp = new ContentVo();
            temp.setCid(cid);
            temp.setHits(chits + hits);
            contentService.updateContentByCid(temp);
            cache.hset("article" + cid, "hits", 1);
        } else {
            cache.hset("article" + cid, "hits", hits);
        }
    }

    /**
     * 检查同一个ip地址是否在2小时内访问同一文章
     *
     * @param request
     * @param cid
     * @return
     */
    private boolean checkHitsFrequency(HttpServletRequest request, String cid) {
        String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.HITS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return true;
        }
        cache.hset(Types.HITS_FREQUENCY.getType(), val, 1, WebConst.HITS_LIMIT_TIME);
        return false;
    }

    /**
     * 搜索页
     *
     * @param servletRequest
     * @param keyword
     * @param limit
     * @return
     */
    @GetMapping(value = "search/{keyword}")
    public String search(HttpServletRequest servletRequest,
                         @PathVariable String keyword,
                         @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.search(servletRequest, keyword, 1, limit);
    }


    public String search(HttpServletRequest request,
                         @PathVariable String keyword,
                         @PathVariable int page,
                         @RequestParam(value = "limit", defaultValue = "12") int limit) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        PageInfo<ContentVo> articles = contentService.getArticles(keyword, page, limit);
        request.setAttribute("articles", articles);
        request.setAttribute("type", "搜索");
        request.setAttribute("keyword", keyword);
        return this.render("page-category");
    }

    /**
     * 评论操作
     *
     * @param request
     * @param response
     * @param cid
     * @param coid
     * @param author
     * @param mail
     * @param url
     * @param text
     * @param _csrf_token
     * @return
     */
    @PostMapping(value = "/comment")
    @ResponseBody
    public RestResponseBo comment(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "cid") Integer cid,
                                  @RequestParam(value = "coid") Integer coid,
                                  @RequestParam(value = "author") String author,
                                  @RequestParam(value = "mail") String mail,
                                  @RequestParam(value = "url") String url, @RequestParam(value = "text") String text,
                                  @RequestParam(value = "_csrf_token") String _csrf_token) {
        String ref = request.getHeader("Referer");
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(_csrf_token)) {
            return RestResponseBo.fail(ErrorCode.BAD_REQUEST);
        }
        String token = cache.hget(Types.CSRF_TOKEN.getType(), _csrf_token);
        if (StringUtils.isBlank(token)) {
            return RestResponseBo.fail(ErrorCode.BAD_REQUEST);
        }

        if (null == cid || StringUtils.isBlank(text)) {
            return RestResponseBo.fail("请输入完整后评论");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return RestResponseBo.fail("姓名过长");
        }

        if (StringUtils.isNotBlank(mail) && !TaleUtils.isEmail(mail)) {
            return RestResponseBo.fail("请输入正确的邮箱格式");
        }

        if (StringUtils.isNotBlank(url) && !PatternKit.isURL(url)) {
            return RestResponseBo.fail("请输入正确的URL格式");
        }

        if (text.length() > 200) {
            return RestResponseBo.fail("请输入200个字符以内的评论");
        }
        String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return RestResponseBo.fail("您发表评论太快了，请过会再试");
        }

        author = TaleUtils.cleanXSS(author);
        text = TaleUtils.cleanXSS(text);

        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        CommentVo comments = new CommentVo();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(coid);

        try {
            String result = commentService.insertComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(mail, "UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url", URLEncoder.encode(url, "UTF-8"), 7 * 24 * 60 * 60, response);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY.getType(), val, 1, 60);
            if (!WebConst.SUCCESS_RESULT.equals(result)) {
                return RestResponseBo.fail(result);
            }
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "评论发布失败";
            LOGGER.error(msg, e);
            return RestResponseBo.fail(msg);
        }
    }


    /**
     * 设置cookie
     *
     * @param name
     * @param value
     * @param maxAge
     * @param response
     */
    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

    /**
     * 注销 登出
     *
     * @param session
     * @param response
     */
    @GetMapping(value = "/logout")
    public void logout(HttpSession session, HttpServletResponse response) {
        TaleUtils.logout(session, response);
    }
}
