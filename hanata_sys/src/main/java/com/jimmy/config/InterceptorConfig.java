package com.jimmy.config;

import com.jimmy.bean.url.UserUrl;
import com.jimmy.servlet.AuthenticationInterceptor;
import com.jimmy.servlet.UrlInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-06
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    UserUrl userUrl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> allowUrlList = userUrl.allowUrlList();

        registry.addInterceptor(urlInterceptor())
                .excludePathPatterns("/error");
        registry.addInterceptor(authenticationInterceptor())
//                .addPathPatterns(allowUrlList);
                 .excludePathPatterns("/error")
                 .excludePathPatterns("/api/captcha")
                  .addPathPatterns("/**");
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Bean
    public UrlInterceptor urlInterceptor(){
        return new UrlInterceptor();
    }
}
