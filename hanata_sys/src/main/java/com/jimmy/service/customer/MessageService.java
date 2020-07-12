package com.jimmy.service.customer;

import com.github.pagehelper.Page;
import com.jimmy.bean.Message;
import com.jimmy.bean.User;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.UserMapper;
import com.jimmy.mapper.customer.MessageMapper;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-13
 */

@Service
@Transactional
public class MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder encoder;

    // 预留
//    public Integer newMessage(String userId,String title,String content) throws CustomException {
//        User userById = userMapper.getUserById(userId);
//        if(userById==null){
//            throw new CustomException("参数错误");
//        }
//
//        Date datetime = Calendar.getInstance().getTime();
//        String uid = String.valueOf(mySnowFlake.nextId());
//        return messageMapper.sendMessage(uid, datetime, title, content, userId);
//    }

    public Message showMessage(String uid) throws CustomException {
        User user = RequestContext.get().getUser();

        Date datetime = Calendar.getInstance().getTime();
        Message message = messageMapper.showMessage(uid, user.getUid());
        if(message==null){
            throw new CustomException("参数错误");
        }
        messageMapper.readMessage(uid,datetime);

        return message;
    }


    public Page<Message> listMessage(String read) throws CustomException {
        User user = RequestContext.get().getUser();
        if (read.equals("0")){
            return messageMapper.listMessage(user.getUid());
        }
        else if(read.equals("1")){
            return messageMapper.listUnreadMessage(user.getUid());
        }
        else{
            throw new CustomException("参数错误");
        }
    }

    public Integer unReadMessage(){
        User user = RequestContext.get().getUser();
        return messageMapper.unReadMessage(user.getUid());
    }

    public Integer readAllMessage(){
        User user = RequestContext.get().getUser();
        return messageMapper.readAll(user.getUid());
    }
}
