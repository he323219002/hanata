package com.jimmy.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jimmy He
 * @date 2020-06-16
 */

@Configuration
public class ESConfig {

    @Value("${elasticsearch.hostname}")
    private String ESHost;

    @Value("${elasticsearch.port}")
    private Integer ESPort;

    @Value("${elasticsearch.scheme}")
    private String ESScheme;

    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(ESHost, ESPort, ESScheme)));
        return client;
    }
}
