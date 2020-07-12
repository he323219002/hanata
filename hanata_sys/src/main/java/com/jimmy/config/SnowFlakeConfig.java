package com.jimmy.config;

import com.jimmy.utils.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jimmy He
 * @date 2020-06-06
 */
@Configuration
public class SnowFlakeConfig {

    @Bean
    public SnowFlake mySnowFlake(){
        return new SnowFlake(1,1);
    }
}
