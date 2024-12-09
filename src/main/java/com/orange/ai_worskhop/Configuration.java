package com.orange.ai_worskhop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${weaviate.protocol}")
    private String weaviateProtocol;

    @Value("${weaviate.url}")
    private String weaviateUrl;
    
    @Bean
    public WeaviateClient weaviateClient() {
        Config config = new Config(weaviateProtocol, weaviateUrl);
        return new WeaviateClient(config);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
