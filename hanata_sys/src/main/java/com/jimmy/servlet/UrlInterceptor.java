package com.jimmy.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-23
 */
public class UrlInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    WebApplicationContext applicationContext;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        ArrayList<String> urlList = new ArrayList<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            PatternsRequestCondition pattern = info.getPatternsCondition();
            urlList.addAll(pattern.getPatterns());
        }

        String requestURL = request.getRequestURI();
        if (!urlList.contains(requestURL)){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return true;
    }
}
