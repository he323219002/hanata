package com.jimmy.controller.system;

import com.jimmy.repository.ResetPwdRepository;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-18
 */

@RestController
@RequestMapping("/api/redirect/")
public class RedirectController {

    @Autowired
    ResetPwdRepository resetPwdRepository;

    @PassToken
    @GetMapping("/article")
    public HaResponse redirectArticle(@RequestParam("articleId") String articleId){
        // frontend redirect
        HaResponse response = new HaResponse();
        response.setData(articleId);
        return response;
    }

    @PassToken
    @GetMapping("/cancel")
    public HaResponse redirectCancelSub(@RequestParam("userId") String userId){
        // frontend redirect
        HaResponse response = new HaResponse();
        response.setData(userId);
        return response;
    }

    @PassToken
    @GetMapping("/resetPwd")
    public HaResponse redirectResetPwd(@RequestParam("userId") String userId,@RequestParam("token") String token){
        Map<Object, Object> reqMap = new HashMap<>() {
            {
                put("userId", userId);
                put("token", token);

            }
        };

        HaResponse response = new HaResponse();
        response.setData(reqMap);
        return response;
    }


}
