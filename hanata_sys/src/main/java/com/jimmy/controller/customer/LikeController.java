package com.jimmy.controller.customer;

import com.jimmy.exception.CustomException;
import com.jimmy.service.customer.LikeService;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-13
 */

@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired
    LikeService likeService;

    @PostMapping("/new")
    public HaResponse newLike(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String articleId = (String) reqMap.getOrDefault("articleId","");
        if (articleId.equals("")){
            throw new CustomException("参数必填");
        }

        Integer count = likeService.newFavour(articleId);
        if (count==1){
            return new HaResponse();
        }else{
            throw new CustomException("点赞失败");
        }
    }

    @PostMapping("/cancel")
    public HaResponse cancelLike(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String articleId = (String) reqMap.getOrDefault("articleId","");
        if (articleId.equals("")){
            throw new CustomException("参数必填");
        }

        Integer count = likeService.cancelFavour(articleId);
        if (count==1){
            return new HaResponse();
        }else{
            throw new CustomException("取消失败");
        }
    }

    @PassToken
    @PostMapping("")
    public HaResponse listLike(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String articleId = (String) reqMap.getOrDefault("articleId", "");
        if (articleId.equals("")){
            throw new CustomException("参数必填");
        }
        List list = likeService.listLike(articleId);
        HaResponse response = new HaResponse();
        response.setData(list);
        return response;
    }
}
