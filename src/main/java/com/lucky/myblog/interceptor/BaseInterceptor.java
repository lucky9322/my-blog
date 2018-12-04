package com.lucky.myblog.interceptor;

import com.lucky.myblog.util.Commons;
import com.lucky.myblog.util.IPKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/4.
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Resource
    private Commons commons;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contextPath = request.getContextPath();
        LOGGE.info("站点的根路径:" + contextPath);
        String uri = request.getRequestURI();
        LOGGE.info("UserAgent: {}", request.getHeader(USER_AGENT));
        LOGGE.info("用户访问地址: {}, 来路地址: {}", uri, IPKit.getIpAddrByRequest(request));

        // TODO: 2018/12/4 添加请求拦截，配置缓存
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        request.setAttribute("commons", commons);//一些工具类和公共方法
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
