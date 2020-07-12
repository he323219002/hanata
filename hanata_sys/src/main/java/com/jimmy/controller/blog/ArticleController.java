package com.jimmy.controller.blog;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.bean.Article;
import com.jimmy.bean.Category;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.service.blog.ArticleService;
import com.jimmy.service.blog.LabelService;
import com.jimmy.servlet.AuthenticationInterceptor;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.CommonUtils;
import com.jimmy.utils.HaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jimmy He
 * @date 2020-06-11
 */

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    ArticleService articleService;

    @PostMapping("/test")
    public String test(@RequestBody Map<String, Object> reqMap){
        logger.error("bad");
        return "ok";
    }

    @PostMapping("/new")
    public HaResponse newArticle(@RequestBody Map<String, Object> reqMap) throws CustomException, IOException, AlreadyExistsException, MessagingException {

        HaResponse response = new HaResponse();

        String title = (String) reqMap.getOrDefault("title","");
        String content = (String) reqMap.getOrDefault("content","");
        String categoryId = (String) reqMap.getOrDefault("cid","");
        String state = (String) reqMap.getOrDefault("state", "1");
        ArrayList<Object> emptyList = new ArrayList<>();
        List<String> labelIdList = (List<String>) reqMap.getOrDefault("lid",emptyList);
        String open = (String) reqMap.getOrDefault("open", "1");
        String commentOpen = (String) reqMap.getOrDefault("commentOpen", "1");
        Integer count = articleService.newArticle(title, content, categoryId, labelIdList, open, commentOpen,state);


        if (count==1){
            return new HaResponse();
        }
        return new HaResponse(1,"删除失败");
    }



    @PostMapping("/del")
    public HaResponse delArticle(@RequestBody Map<String, Object> reqMap) throws CustomException, IOException {
        String article_id = (String) reqMap.getOrDefault("id","");
        Integer count = articleService.delArticle(article_id);
        if (count == 1) {
            return new HaResponse();
        }
        return new HaResponse(1,"删除失败");

    }

    @PostMapping("/update")
    public HaResponse updateArticle(@RequestBody Map<String, Object> reqMap) throws CustomException, AlreadyExistsException, IOException {
        String uid = (String)reqMap.getOrDefault("uid","");
        String title = (String)reqMap.getOrDefault("title","");
        String content = (String) reqMap.getOrDefault("content","");
        String categoryId = (String) reqMap.getOrDefault("cid","");
        String open = (String) reqMap.get("open");
        String commentOpen = (String) reqMap.get("commentOpen");
        String state = (String) reqMap.getOrDefault("state","1");
        ArrayList<Object> emptyList = new ArrayList<>();

        List<String> delLabel = (List<String>) reqMap.getOrDefault("delLabel",emptyList);
        List<String> newLabel = (List<String>) reqMap.getOrDefault("newLabel",emptyList);

        Integer integer = articleService.updateArticle(uid, title, content, categoryId, open, commentOpen, delLabel, newLabel,state);
        if (integer==1){
            return new HaResponse();
        }
        return new HaResponse(1,"添加失败,可能重复");
    }

    @PassToken
    @PostMapping("/show")
    public HaResponse showArticle(@RequestBody Map<String, Object> reqMap) throws CustomException {
        String uid = (String) reqMap.getOrDefault("uid","");
        Map map = articleService.showArticle(uid);
        HaResponse response = new HaResponse();
        response.setData(map);
        return response;
    }

    @PassToken
    @PostMapping("")
    public HaResponse ListArticle(@RequestBody Map<String,Object> qMap) throws CustomException {
        String limit = (String)qMap.getOrDefault("limit", "1");
        String offset = (String) qMap.getOrDefault("offset", "10");
        String admin = (String) qMap.getOrDefault("admin", "0");
        String q = (String) qMap.getOrDefault("q","");
        String cid = (String) qMap.getOrDefault("cid","");
        String lid = (String) qMap.getOrDefault("lid","");
        String state = (String) qMap.getOrDefault("state","");

        PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
        Page<Article> articles = articleService.listArticle(admin,q,cid,lid,state);
        for (Article article : articles) {
            String content = article.getContent();
            if (content.length()>=200){
                String newContent = content.substring(0, 200) + "……";
                article.setContent(newContent);
            }
        }

        PageInfo<Article> pageInfo = new PageInfo<>(articles);

        HaResponse response = new HaResponse();
        response.setData(pageInfo);

        return response;
    }

}
