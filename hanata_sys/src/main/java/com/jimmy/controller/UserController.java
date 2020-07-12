package com.jimmy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.auth0.jwt.interfaces.Claim;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.bean.User;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.service.UserService;
import com.jimmy.servlet.PassToken;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.HaResponse;
import com.jimmy.utils.JwtTokenUtil;
import com.jimmy.utils.RedisUtil;
import com.jimmy.utils.ThrottleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.mail.MessagingException;
import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-05
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    ServletRequest servletRequest;

    @Autowired
    UserService userService;

    @Autowired
    ThrottleUtil throttleUtil;

    @Autowired
    RedisUtil redisUtil;


    @PassToken
    @PostMapping("/admin/new")
    public HaResponse createAdmin(@RequestBody Map<String, Object> reqMap) throws CustomException {

        String username = (String) reqMap.getOrDefault("username", "");
        String password = (String) reqMap.getOrDefault("password", "");
        String phone = (String) reqMap.getOrDefault("phone", "");
        String mail = (String) reqMap.getOrDefault("mail", "");

        try {
            Integer count = userService.createAdmin(username, password, phone, mail);
            if (count == 1) {
                return new HaResponse();
            } else {
                throw new CustomException("参数错误");
            }
        } catch (AlreadyExistsException e) {
            throw new CustomException(e.getMessage());
        }
    }


    @PostMapping("/show")
    public HaResponse getUser(@RequestBody Map<String,String> reqMap) throws CustomException {
        String id = reqMap.getOrDefault("id","");


        User user = userService.getUser(id);
        if(user==null){
            throw new CustomException("参数错误");
        }
        HaResponse response = new HaResponse();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter("username", "nickname","avatar","mail","gender");
        Map<String, Object> res = JSONObject.parseObject(JSONObject.toJSONString(user, filter));
        response.setData(res);

        return response;
    }

    // todo 增加登录限制
    @PassToken
    @PostMapping("/login")
    public HaResponse login(@RequestBody Map<String, Object> reqMap) throws CustomException {
        String username = (String) reqMap.getOrDefault("username", "");
        String password = (String) reqMap.getOrDefault("password", "");
        String formId = (String) reqMap.getOrDefault("formId", "");
        String captcha = (String) reqMap.getOrDefault("captcha", "");


        User user = userService.getUserbyUsername(username);
        if (user == null) {
            throttleUtil.record(username);
            throw new CustomException("用户名不存在");
        }
        boolean bool = userService.checkUserCredential(user, password);
        if (!bool){
            throttleUtil.record(username);
            throw new CustomException("密码错误");
        }

        if (formId.equals("")){
            throw new CustomException("参数必填");
        }
        if (captcha.equals("")){
            throw new CustomException("验证码不能为空");
        }
        String realCaptcha = (String) redisUtil.get(formId);
        if(!captcha.equals(realCaptcha)){
            throttleUtil.record(username);
            throw new CustomException("验证码错误");
        }
        // define login data fields
        HaResponse response = new HaResponse();
        String token = JwtTokenUtil.createToken(user);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("nickname",user.getNickname());
        tokenMap.put("mail",user.getMail());
        tokenMap.put("avatar",user.getAvatar());
        tokenMap.put("token",token);

        // put login info in map
        Map<String, Claim> claimMap = JwtTokenUtil.decodeToken(token);
        Claim userJsonObj = claimMap.get("userJson");
        if(userJsonObj==null){
            throw new CustomException("invalid token");
        }
        String userJson = userJsonObj.asString();
        User userField = JSON.parseObject(userJson, User.class);

        String role = userField.getRole();


        tokenMap.put("role",role);

        response.setData(tokenMap);
        redisUtil.del(formId);

        return response;
    }



    @PassToken
    @PostMapping("/reg")
    public HaResponse register(@RequestBody Map<String,String> reqMap) throws AlreadyExistsException, CustomException {
        String username = reqMap.getOrDefault("username","");
        String password = reqMap.getOrDefault("password","");
        String nickname = reqMap.getOrDefault("nickname","");
        String avatar = reqMap.getOrDefault("avatar","");
        String mail = reqMap.getOrDefault("mail","");
        String gender = reqMap.getOrDefault("gender","");


        userService.register(username,password,nickname,avatar,mail,gender);
        return new HaResponse();
    }

    @PassToken
    @PostMapping("/setPassword")
    public HaResponse resetPwd(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String userID = (String) reqMap.getOrDefault("userID", "");
        String password = (String) reqMap.getOrDefault("password", "");
        String token = (String) reqMap.getOrDefault("token", "");

        userService.resetPwd(userID,password,token);

        return new HaResponse();
    }

    @PassToken
    @PostMapping("/reset")
    public HaResponse forgetPwd(@RequestBody Map<String,Object> reqMap) throws CustomException, MessagingException {
        String mail = (String) reqMap.getOrDefault("mail", "");

        userService.forgetPwd(mail);

        HaResponse response = new HaResponse();
        response.setData("请在您的邮件中重置密码");
        return response;
    }

    @PostMapping("")
    public HaResponse ListUser(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String limit = (String)reqMap.getOrDefault("limit", "1");
        String offset = (String)reqMap.getOrDefault("offset", "10");
        String q = (String) reqMap.getOrDefault("q", "");

        PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
        Page<User> users = userService.listUser(q);

        PageInfo<User> userPageInfo = new PageInfo<>(users);

        return new HaResponse(){
            {
                setData(userPageInfo);
            }
        };
    }

    @PostMapping("/active")
    public HaResponse activeUser(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String active = (String) reqMap.getOrDefault("active", "1");
        String userId = (String) reqMap.getOrDefault("userId", "");

        userService.activeUser(userId,active);
        return new HaResponse();
    }

    @PostMapping("/update")
    public HaResponse updateUser(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String nickname = (String) reqMap.getOrDefault("nickname", "");
        String mail = (String) reqMap.getOrDefault("mail", "");
        String avatar = (String) reqMap.getOrDefault("avatar", "");

        userService.updateUser(nickname,mail,avatar);
        return new HaResponse();
    }


    @PostMapping("/check")
    @PassToken
    public HaResponse checkRegsterData(@RequestBody Map<String,Object> reqMap){
        String type = (String) reqMap.getOrDefault("type", "");
        String value = (String) reqMap.getOrDefault("value", "");
        Integer count;
        String res;
        // 检查username
        if (type.equals("1")){
            count = userService.checkUsername(value);
            res=count==1?"用户名已存在":"";
        }
        // 检查nickname
        else if (type.equals("2")){
            count= userService.checkNickname(value);
            res=count==1?"昵称已存在":"";

        }
        // 检查mail
        else{
            count= userService.checkMail(value);
            res=count==1?"邮箱已存在":"";
        }

        if (count==1){
            return new HaResponse(1, res);
        }
        return new HaResponse();
    }
}
