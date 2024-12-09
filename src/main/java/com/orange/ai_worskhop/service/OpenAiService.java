package com.orange.ai_worskhop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.orange.ai_worskhop.domain.OpenAiRequest;
import com.orange.ai_worskhop.domain.OpenAiResponse;

@Service
public class OpenAiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    public String getChatCompletion(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        OpenAiRequest request = new OpenAiRequest(
            "gpt-4o-mini",
            List.of(new OpenAiRequest.Message("user", prompt)),
            0.7
        );
        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<OpenAiResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, OpenAiResponse.class);
        
        return response.getBody().choices().get(0).message().content();
    }

}
