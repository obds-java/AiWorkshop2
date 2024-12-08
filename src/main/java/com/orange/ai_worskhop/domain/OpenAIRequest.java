package com.orange.ai_worskhop.domain;

import java.util.List;

/*
 * {
 * "model": "gpt-4o-mini",
 * "messages": [{"role": "user", "content": "Say this is a test!"}],
 * "temperature": 0.7
 * }
 */
public record OpenAIRequest(String model, List<Message> messages, double temperature) {
    public OpenAIRequest(String model, String prompt, double temperature) {
        this(model, List.of(new Message("user", prompt)), temperature);
    }

    public static record Message(String role, String content) {
    }
}
