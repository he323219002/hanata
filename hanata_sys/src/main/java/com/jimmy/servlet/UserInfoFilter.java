package com.jimmy.servlet;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.jimmy.bean.User;
import com.jimmy.utils.JwtTokenUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-08
 */

@WebServlet(urlPatterns = {"/*"})
public class UserInfoFilter implements Filter {

    private ServletContext context;


//    @Autowired
//    ThreadLocal<String> threadLocal;

    @Override
    public void destroy() {
        RequestContext.get().end();
    }

    // add user json in request
    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;


        String token = httpRequest.getHeader("Authorization");

//      decode token get user bean
        if(token!=null&&!token.equals("")){
            Map<String, Claim> claimMap = JwtTokenUtil.decodeToken(token);
            Claim userJsonObj = claimMap.get("userJson");
            String userJson = userJsonObj.asString();
            User user = JSON.parseObject(userJson, User.class);

            // can load user info in different place
            RequestContext.begin(this.context,httpRequest,httpResponse,user);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }


}
