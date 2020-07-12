package com.jimmy.servlet;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.jimmy.bean.User;
import com.jimmy.bean.es.EsRecord;
import com.jimmy.exception.CustomException;
import com.jimmy.service.UserService;
import com.jimmy.service.system.ESService;
import com.jimmy.utils.HaResponse;
import com.jimmy.utils.RecordUtil;
import com.jimmy.utils.RedisUtil;
import com.jimmy.utils.ThrottleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-06
 */
public class AuthenticationInterceptor implements HandlerInterceptor {


    Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    UserService userService;

    @Autowired
    ESService esService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ThrottleUtil throttleUtil;


    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,Object object) throws Exception {
        String token = httpServletRequest.getHeader("Authorization");
        // if object not method -> pass
        if(!(object instanceof HandlerMethod)){
            return true;
        }

        // record visit info and set restrict of request times per min
        EsRecord esRecord = RecordUtil.recordRequest(httpServletRequest);
        esService.newRecord(esRecord);

        String IP = httpServletRequest.getRemoteAddr();

        throttleUtil.setRestriction(IP);

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //check if method has passtoken annotation

        if(method.isAnnotationPresent(PassToken.class)){
            PassToken passtoken = method.getAnnotation(PassToken.class);
            if(passtoken.required()){
                return true;
            }
        }else{
            if(token == null){
                logger.error("invalid token");
                throw new CustomException("未获取到token");
            }

            // check user
            String userId;
            try{
                userId = JWT.decode(token).getAudience().get(0);
            }catch (JWTDecodeException e){
                logger.error("failed to decode token");
                throw new CustomException("token解析异常");
            }
            User user = userService.getUser(userId);
            if(user == null){
                logger.error("user doesn't exist");
                throw new CustomException("用户不存在");
            }

            // check token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            try{
                jwtVerifier.verify(token);
            }catch (JWTVerificationException e){
                logger.error("invalid token2");
                throw new CustomException("token 验证异常");
            }
            return true;
        }


        return true;
    }

}
