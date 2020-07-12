package com.jimmy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-16
 */

@Configuration
@ConfigurationProperties("aliyun")
public class PropertyConfig {
    private List<String> fileType;

    public List<String> getFileType() {
        return fileType;
    }

    public void setFileType(List<String> fileType) {
        this.fileType = fileType;
    }
}
