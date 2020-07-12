package com.jimmy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-16
 */
@Data
@Configuration
public class OSSConfig implements Serializable {


    @Value("${aliyun.accessKey}")
    private String accessKey;

    @Value("${aliyun.secret}")
    private String secret;

    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.bucket}")
    private String bucket;

}
