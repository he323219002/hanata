package com.jimmy.bean.url;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-09
 */

@Data
@Component
@ConfigurationProperties(prefix = "allowurl.appuser")
public class UserUrl{
    private String prefix;
    private List<String> uri;
    private List<String> allowUri;

    public void setAllowUri(List<String> allowUri) {
        this.allowUri = allowUri;
    }

    public List<String> allowUrlList(){
        List<String> urlList = new ArrayList<>();

        for (String s : uri) {
            urlList.add("/api"+prefix+s);
        }
        return urlList;
    }

}
