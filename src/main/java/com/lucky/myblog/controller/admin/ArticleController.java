package com.lucky.myblog.controller.admin;

import com.lucky.myblog.controller.BaseController;
import com.lucky.myblog.dto.Types;
import com.lucky.myblog.model.vo.MetaVo;
import com.lucky.myblog.service.IMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Project: my-blog
 * Created by Zhangdd on 2018/12/7.
 */
@Controller
@RequestMapping(value = "/admin/article")
public class ArticleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private IMetaService metasService;


    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request) {
        List<MetaVo> categories = metasService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        return "admin/article_edit";
    }
}
