package com.jimmy.repository;

import com.jimmy.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Jimmy He
 * @date 2020-06-18
 */

@Repository
public class ResetPwdRepository {

    @Autowired
    RedisUtil redisUtil;

    public void setUsertoken(String userId){

        String token = UUID.randomUUID().toString();
        String tokenRecord = "token_" + userId;

        redisUtil.set(userId,token,15*60);
        redisUtil.set(tokenRecord,0L,15);
    }

}
