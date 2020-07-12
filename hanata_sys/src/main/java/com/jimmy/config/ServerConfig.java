package com.jimmy.config;

import com.jimmy.bean.User;
import com.jimmy.bean.url.UserUrl;
import com.jimmy.servlet.UserInfoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-08
 */

@Configuration
public class ServerConfig {

    @Autowired
    UserUrl userUrl;

    @Bean
    public FilterRegistrationBean myFilter(){
        List<String> allowUrlList = userUrl.allowUrlList();
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new UserInfoFilter());
//        filterFilterRegistrationBean.setUrlPatterns(allowUrlList);
//        filterFilterRegistrationBean.setUrlPatterns(Arrays.asList("/api/user/test","api/user"));
        return filterFilterRegistrationBean;
    }

}
