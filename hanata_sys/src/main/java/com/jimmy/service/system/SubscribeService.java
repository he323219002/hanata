package com.jimmy.service.system;

import com.jimmy.bean.User;
import com.jimmy.bean.redis.SubModel;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.UserMapper;
import com.jimmy.repository.SubscribeRepository;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-18
 */
@Service
public class SubscribeService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SubscribeRepository subscribeRepository;

    public void subscribe() throws AlreadyExistsException {
        User user = RequestContext.get().getUser();
        String userId = user.getUid();
        String mail = user.getMail();
        String nickname = user.getNickname();
        boolean exist = redisUtil.hHasKey("subscribeList", userId);
        if (exist){
            throw new AlreadyExistsException("已经订阅过");
        }

        SubModel subModel = new SubModel();
        subModel.setMail(mail);
        subModel.setNickname(nickname);


        Map<String, Object> userMap = new HashMap<>() {
            {
                put(userId, subModel);
            }
        };

        subscribeRepository.subscribe(userMap);
    }

    public void cancelSub(String userId) throws CustomException {
        if(userId.equals("")){
            throw new CustomException("参数必填");
        }

        User userById = userMapper.getUserById(userId);
        if (userById==null){
            throw new CustomException("参数错误");
        }

        boolean exist = redisUtil.hHasKey("subscribeList", userId);
        if (!exist){
            throw new CustomException("您未订阅");
        }

        subscribeRepository.cancelSub(userId);

    }
}
