package com.orange.ai_worskhop.domain;

import java.util.List;

/*
 * {
    "id": "chatcmpl-abc123",
    "object": "chat.completion",
    "created": 1677858242,
    "model": "gpt-4o-mini",
    "usage": {
        "prompt_tokens": 13,
        "completion_tokens": 7,
        "total_tokens": 20,
        "completion_tokens_details": {
            "reasoning_tokens": 0,
            "accepted_prediction_tokens": 0,
            "rejected_prediction_tokens": 0
        }
    },
    "choices": [
        {
            "message": {
                "role": "assistant",
                "content": "\n\nThis is a test!"
            },
            "logprobs": null,
            "finish_reason": "stop",
            "index": 0
        }
    ]
}
 */
public record OpenAiResponse(
    String id,
    String object,
    long created,
    String model,
    Usage usage,
    List<Choice> choices
) {
    public record Usage(
        int prompt_tokens,
        int completion_tokens,
        int total_tokens,
        CompletionTokensDetails completion_tokens_details
    ) {
        public record CompletionTokensDetails(
            int reasoning_tokens,
            int accepted_prediction_tokens,
            int rejected_prediction_tokens
        ) {}
    }

    public record Choice(
        Message message,
        String logprobs,
        String finish_reason,
        int index
    ) {
        public record Message(
            String role,
            String content
        ) {}
    }
}