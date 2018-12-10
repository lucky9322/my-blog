package com.lucky.myblog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.lucky.myblog.controller.BaseController;
import com.lucky.myblog.model.bo.RestResponseBo;
import com.lucky.myblog.model.vo.CommentVo;
import com.lucky.myblog.model.vo.CommentVoExample;
import com.lucky.myblog.model.vo.UserVo;
import com.lucky.myblog.service.ICommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/10.
 */
@Controller
@RequestMapping(value = "/admin/comments")
public class CommentController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    @Resource
    private ICommentService commentsService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit) {
        UserVo users = this.user(request);
        CommentVoExample commentVoExample = new CommentVoExample();
        commentVoExample.setOrderByClause("coid desc");
        commentVoExample.createCriteria().andAuthorIdNotEqualTo(users.getUid());
        PageInfo<CommentVo> commentsPaginator = commentsService.getCommentsWithPage(commentVoExample, page, limit);
        request.setAttribute("comments", commentsPaginator);
        return "admin/comment_list";
    }

    /**
     * 删除一条评论
     *
     * @param coid
     * @return
     */
    @PostMapping(value = "delete")
    @ResponseBody
    public RestResponseBo delete(@RequestParam Integer coid) {
        try {
            CommentVo comments = commentsService.getCommentById(coid);
            if (null == comments) {
                return RestResponseBo.fail("不存在该评论");
            }
            commentsService.delete(coid, comments.getCid());
        } catch (Exception e) {
            String msg = "评论删除失败";
            LOGGER.error(msg, e);
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    @PostMapping(value = "status")
    @ResponseBody
    public RestResponseBo delete(@RequestParam Integer coid, @RequestParam String status) {
        try {
            CommentVo comments = commentsService.getCommentById(coid);
            if (comments != null) {
                comments.setCoid(coid);
                comments.setStatus(status);
                commentsService.update(comments);
            } else {
                return RestResponseBo.fail("操作失败");
            }
        } catch (Exception e) {
            String msg = "操作失败";
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }
}
