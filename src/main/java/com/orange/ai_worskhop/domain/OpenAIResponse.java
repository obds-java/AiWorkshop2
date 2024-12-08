package com.orange.ai_worskhop.domain;

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

public record OpenAIResponse(
    String id,
    String object,
    long created,
    String model,
    Usage usage,
    Choice[] choices
) {
    public record Usage(
        int promptTokens,
        int completionTokens,
        int totalTokens,
        CompletionTokensDetails completionTokensDetails
    ) {
        public record CompletionTokensDetails(
            int reasoningTokens,
            int acceptedPredictionTokens,
            int rejectedPredictionTokens
        ) {}
    }

    public record Choice(
        Message message,
        Object logprobs,
        String finishReason,
        int index
    ) {
        public record Message(
            String role,
            String content
        ) {}
    }
}