package com.orange.ai_worskhop.domain;

import java.util.List;

public class Book {
    private Metadata metadata;
    private List<String> chunks;

    public Book(Metadata metadata, List<String> chunks) {
        this.metadata = metadata;
        this.chunks = chunks;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<String> getChunks() {
        return chunks;
    }

    public void setChunks(List<String> chunks) {
        this.chunks = chunks;
    }
}
