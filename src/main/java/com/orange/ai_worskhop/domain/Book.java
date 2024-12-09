package com.orange.ai_worskhop.domain;

import java.util.List;

public record Book(Metadata metadata, List<String> chunks) {
}