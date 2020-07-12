package com.jimmy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.jimmy.bean.User;
import com.jimmy.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-06
 */
public class JwtTokenUtil {


    public static String createToken(User user){
        String userJson = JSON.toJSONString(user);
        String token = JWT.create().withAudience(user.getUid())
                .withClaim("userJson",userJson)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    public static Map<String,Claim> decodeToken(String token) {

        return JWT.decode(token).getClaims();

    }
}
