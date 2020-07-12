package com.jimmy.utils;

import com.jimmy.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jimmy He
 * @date 2020-06-18
 */
@Component
public class ThrottleUtil {

//    private ThrottleUtil(){
//    }
//    public static ThrottleUtil getSingleton(){
//        return Inner.instance;
//    }
//    private static class Inner {
//        private static final ThrottleUtil instance = new ThrottleUtil();
//    }

    @Autowired
    RedisUtil redisUtil;

    public void record(String userId) throws CustomException {
        String record_limit = "record_limit_" + userId;
        String record_times = "record_times_" + userId;

        if (redisUtil.get(record_limit) != null) {
            throw new CustomException("操作太频繁，请5分钟后重试");
        }

        Object times = redisUtil.get(record_times);
        if (times != null) {
            if ((Integer) times == 8) {
                redisUtil.set(record_limit, 1, 5 * 60);
                throw new CustomException("操作太频繁，请5分钟后重试");
            }
            redisUtil.incr(record_times, 1L);
        } else {
            redisUtil.set(record_times, 1L, 5 * 60);
        }
    }

    public void setRestriction(String IP) throws CustomException {
        String request_limit = "request_limit_" + IP;
        String reqeust_times = "request_times_" + IP;

        if (redisUtil.get(request_limit) != null) {
            throw new CustomException("您的访问过于频繁，请休息一会");
        }

        Object times = redisUtil.get(reqeust_times);
        if (times != null) {
            if ((Integer) times == 60) {
                redisUtil.set(request_limit, 1, 60);
                throw new CustomException("您的访问过于频繁，请休息一会");
            }
            redisUtil.incr(reqeust_times, 1L);
        } else {
            redisUtil.set(reqeust_times, 1L, 60);
        }
    }
}
