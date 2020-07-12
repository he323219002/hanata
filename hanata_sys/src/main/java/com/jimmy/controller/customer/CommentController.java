package com.jimmy.controller.customer;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.bean.*;
import com.jimmy.exception.CustomException;
import com.jimmy.service.UserService;
import com.jimmy.service.blog.ArticleService;
import com.jimmy.service.customer.CommentService;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.CommonUtils;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-13
 */

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    @PostMapping("/new")
    public HaResponse newComment(@RequestBody Map<String, Object> reqMap) throws CustomException {
        String articleId = (String) reqMap.getOrDefault("articleId", "");
        String content = (String) reqMap.getOrDefault("content", "");
        String fartherId = (String) reqMap.getOrDefault("fartherId", "");
        String kind = (String) reqMap.getOrDefault("kind", "");

        if (!kind.equals("1") && !kind.equals("2")){
            return new HaResponse(1,"参数错误");
        }

        Integer count = commentService.newComment(articleId, content, fartherId,kind);
        if(count==1){
            return new HaResponse();
        }else{
            return new HaResponse(1,"评论失败");
        }
    }

    @PostMapping("/del")
    public HaResponse delComment(@RequestBody Map<String, Object> reqMap) throws CustomException {
        String commentId = (String) reqMap.getOrDefault("commentId", "");
        Integer count = commentService.delComment(commentId);

        if(count==1){
            return new HaResponse();
        }else{
            return new HaResponse(1,"删除失败");
        }
    }

    @PostMapping("")
    @PassToken
    public HaResponse ListComment(@RequestBody Map<String,Object> qMap) throws CustomException {
        String limit = (String)qMap.getOrDefault("limit", "1");
        String offset = (String)qMap.getOrDefault("offset", "10");
        String articleId = (String) qMap.getOrDefault("articleId", "");
        String kind = (String) qMap.getOrDefault("kind", "");

        String defaultComment = "1";

        Page<Comment> comments;

        if (kind.equals("1")){
            Article articleById = articleService.getArticleById(articleId);
            if (articleById.getCommentOpen().equals("0")){
                defaultComment = "0";
            }
            PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
            comments = commentService.listComment(articleId);
        }

        else if (kind.equals("2")){
            Dairy dairyById = articleService.getDairyById(articleId);
            if (dairyById.getCommentOpen().equals("0")){
                defaultComment = "0";
            }
            PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
            comments = commentService.listDairyComment(articleId);
        }
        else{
            return new HaResponse(1,"参数错误");
        }

        if (defaultComment.equals("0")){
            return new HaResponse(){
                {
                    setData("该评论已关闭");
                }
            };
        }

        for (Comment comment : comments) {
            String deleted = comment.getDeleted();
            if (deleted.equals("1")){
                comment.setContent("该评论已删除");
            }
        }

        PageInfo<Comment> pageInfo = new PageInfo<>(comments);

        return new HaResponse(){
            {
                setData(pageInfo);
            }
        };
    }

    @PostMapping("/self")
    public HaResponse ListMyComment(@RequestBody Map<String,Object> qMap) throws CustomException {
        String limit = (String)qMap.getOrDefault("limit", "1");
        String offset = (String)qMap.getOrDefault("offset", "10");

        PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
        Page<Comment> comments = commentService.listMyComment();

        List<Map> resList = new ArrayList<>();

        for (Comment comment : comments) {
            String datetime = CommonUtils.formatDate(comment.getCreateTime());
            Map<String, String> hashMap = new HashMap<String, String>() {
                {
                    String createUserId = comment.getCreateUserId();
                    User user = userService.getUser(createUserId);
                    put("uid", comment.getUid());
                    put("userName", comment.getUserName());
                    put("articleId",comment.getArticleId());
                    put("fatherContent", comment.getFartherContent());
                    put("userAvatar", user.getAvatar());
                    put("content", comment.getContent().equals("1")?"该评论已删除":comment.getContent());
                    put("datetime",datetime);
                    put("title",comment.getTitle());
                }
            };
            resList.add(hashMap);
        }

        PageInfo<Map> pageInfo = new PageInfo<>(resList);

        return new HaResponse(){
            {
                setData(pageInfo);
            }
        };
    }
}
