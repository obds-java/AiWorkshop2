package com.orange.ai_worskhop.domain;

import java.util.List;

/*
   {
  "model": "gpt-4o-mini",
  "messages": [{"role": "user", "content": "Say this is a test!"}],
  "temperature": 0.7
  }
 */
public record OpenAiRequest(
    String model,
    List<Message> messages,
    double temperature
) {
    public record Message(
        String role,
        String content
    ) {}
}