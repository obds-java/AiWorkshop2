package com.orange.ai_worskhop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.orange.ai_worskhop.domain.OpenAIRequest;
import com.orange.ai_worskhop.domain.OpenAIResponse;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.temperature}")
    private double temperature;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateText(String prompt) {
        OpenAIRequest requestPayload = new OpenAIRequest(model, prompt, temperature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                OpenAIResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().choices()[0].message().content();
        }

        throw new RuntimeException("Failed to generate text. Response: " + response);
    }
}
