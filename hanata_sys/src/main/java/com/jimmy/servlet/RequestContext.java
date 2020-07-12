package com.jimmy.servlet;

import com.jimmy.bean.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jimmy He
 * @date 2020-06-08
 */
public class RequestContext {
    private ServletContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private User user;

    private final static ThreadLocal<RequestContext> contexts = new ThreadLocal<RequestContext>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 初始化请求上下文
     * @param ctx
     * @param req
     * @param res
     */
    public static RequestContext begin(ServletContext ctx, HttpServletRequest req, HttpServletResponse res,User user) {
        RequestContext rc = new RequestContext();
        rc.context = ctx;
        rc.request = req;
        rc.response = res;
        rc.setUser(user);
        contexts.set(rc);
        return rc;
    }

    /**
     * 获取当前请求的上下文
     * @return
     */
    public static RequestContext get(){
        return contexts.get();
    }


    public void end() {
        this.context = null;
        this.request = null;
        this.response = null;
        contexts.remove();
    }

    public ServletContext getContext() {
        return context;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }


    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

}
