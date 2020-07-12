package com.jimmy.repository;

import com.jimmy.bean.redis.SubModel;
import com.jimmy.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Jimmy He
 * @date 2020-06-18
 */

@Repository
public class SubscribeRepository {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedisTemplate redisTemplate;

    public void subscribe(Map<String,Object> userMap){
        redisUtil.hmset("subscribeList",userMap);
    }

    public void cancelSub(String userId){
        redisUtil.hdel("subscribeList",userId);
    }

    public Map<String, SubModel> getSubList(){
        Map<String, SubModel> subscribeList = redisTemplate.opsForHash().entries("subscribeList");
//        Collection<SubModel> values = subscribeList.values();
        return subscribeList;
    }

    public SubModel getSub(String userId){
        SubModel subModel = (SubModel) redisUtil.hget("subscribeList", userId);
        return subModel;
    }
}
