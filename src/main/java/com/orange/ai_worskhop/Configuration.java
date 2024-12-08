package com.orange.ai_worskhop;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;

// TODO move the properties to application.yml using an LLM
@org.springframework.context.annotation.Configuration
public class Configuration {
    
    @Bean
    public WeaviateClient weaviateClient() {
        Config config = new Config("http", "localhost:8081");
        return new WeaviateClient(config);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
