package com.jimmy.servlet;

import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author Jimmy He
 * @date 2020-06-06
 */
@RestControllerAdvice
public class ExceptionHandler {

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public Object handlerException(CustomException e){
        HashMap<Object, Object> map = new HashMap<>();
        map.put("code",1);
        map.put("msg",e.getMessage());
        return map;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(AlreadyExistsException.class)
    public Object handlerException(AlreadyExistsException e){
        HashMap<Object, Object> map = new HashMap<>();
        map.put("code",1);
        map.put("msg",e.getMessage());
        return map;
    }
}
