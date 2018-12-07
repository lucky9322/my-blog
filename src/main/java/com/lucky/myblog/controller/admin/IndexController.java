package com.lucky.myblog.controller.admin;

import com.lucky.myblog.constant.WebConst;
import com.lucky.myblog.controller.BaseController;
import com.lucky.myblog.dto.LogActions;
import com.lucky.myblog.exception.TipException;
import com.lucky.myblog.model.bo.RestResponseBo;
import com.lucky.myblog.model.bo.StatisticsBo;
import com.lucky.myblog.model.vo.CommentVo;
import com.lucky.myblog.model.vo.ContentVo;
import com.lucky.myblog.model.vo.LogVo;
import com.lucky.myblog.model.vo.UserVo;
import com.lucky.myblog.service.ILogService;
import com.lucky.myblog.service.ISiteService;
import com.lucky.myblog.service.IUserService;
import com.lucky.myblog.util.GsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/5.
 */
@Controller("adminIndexController")
@RequestMapping(value = "/admin")
@Transactional(rollbackFor = TipException.class)
public class IndexController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);


    @Resource
    private ISiteService siteService;

    @Resource
    private ILogService logService;

    @Resource
    private IUserService userService;


    @GetMapping(value = {"/", "/index"})
    public String index(HttpServletRequest request) {
        LOGGER.info("Enter admin index method");

        List<CommentVo> comments = siteService.recentComments(5);
        List<ContentVo> contents = siteService.recentContents(5);
        StatisticsBo statistics = siteService.getStatistics();
        // 取最新的20条日志
        List<LogVo> logs = logService.getLogs(1, 5);

        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);
        request.setAttribute("logs", logs);
        LOGGER.info("Exit admin index method");
        return "admin/index";
    }


    @GetMapping(value = "/profile")
    public String profile() {
        return "admin/profile";
    }
    /**
     * 保存个人信息
     */
    @PostMapping(value = "/profile")
    @ResponseBody
    public RestResponseBo saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {
        UserVo users = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            UserVo temp = new UserVo();
            temp.setUid(users.getUid());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateByUid(temp);
            logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            UserVo original= (UserVo)session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY,original);
        }
        return RestResponseBo.ok();
    }

}
