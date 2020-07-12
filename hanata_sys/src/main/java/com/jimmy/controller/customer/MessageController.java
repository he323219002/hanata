package com.jimmy.controller.customer;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.bean.Label;
import com.jimmy.bean.Message;
import com.jimmy.exception.CustomException;
import com.jimmy.service.customer.MessageService;
import com.jimmy.utils.CommonUtils;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Jimmy He
 * @date 2020-06-13
 */
@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    MessageService messageService;


    @PostMapping("/show")
    public HaResponse showMessage(@RequestBody Map<String, Object> reqMap) throws CustomException {
        String messageId = (String) reqMap.getOrDefault("messageId", "");
        if (messageId.equals("")){
            throw new CustomException("参数必填");
        }

        Message message = messageService.showMessage(messageId);
        String title = message.getTitle();
        String content = message.getContent();
        String datetime = CommonUtils.formatDate(message.getCreateTime());
        Map<String, String> resMap = new HashMap<String, String>() {{
            put("title", title);
            put("content", content);
            put("datetime", datetime);
        }};
        return new HaResponse(){
            {setData(resMap);}
        };
    }

    @PostMapping("")
    public HaResponse listMessage(@RequestBody Map<String,Object> qMap) throws CustomException {
        String limit = (String)qMap.getOrDefault("limit", "1");
        String offset = (String)qMap.getOrDefault("offset", "10");
        // 0 all 1 unread
        String read = (String) qMap.getOrDefault("read", "0");

        PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
        Page<Message> messages = messageService.listMessage(read);


        PageInfo<Message> pageInfo = new PageInfo<>(messages);

        HaResponse response = new HaResponse();
        response.setData(pageInfo);

        return response;
    }

    @PostMapping("/unread")
    public HaResponse unreadMessage(){
        try{
            messageService.unReadMessage();
        }catch (Exception e){
            return new HaResponse(){
                {
                    setCode(1);
                    setMsg("failure");
                    setData(e.getMessage());
                }
            };
        }


        return new HaResponse();
    }

    @PostMapping("/readall")
    public HaResponse readAllMessage(){
        try{
            messageService.readAllMessage();
        }catch (Exception e){
            return new HaResponse(){
                {
                    setCode(1);
                    setMsg("failure");
                    setData(e.getMessage());
                }
            };
        }

        return new HaResponse();
    }
}