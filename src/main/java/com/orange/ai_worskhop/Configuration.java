package com.orange.ai_worskhop;

import org.springframework.context.annotation.Bean;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;

@org.springframework.context.annotation.Configuration
public class Configuration {
    
    @Bean
    public WeaviateClient weaviateClient() {
        Config config = new Config("http", "localhost:8081");
        return new WeaviateClient(config);
    }
}
