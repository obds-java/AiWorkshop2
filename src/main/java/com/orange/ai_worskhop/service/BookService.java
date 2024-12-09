package com.orange.ai_worskhop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.orange.ai_worskhop.domain.Metadata;

@Service
public class BookService {

    public Metadata extractMetadata(String html) {
        Document document = Jsoup.parse(html);
        Element container = document.getElementById("pg-machine-header");

        String title = container.select("p:contains(Title)").text().replace("Title: ", "");
        String author = container.select("#pg-header-authlist p:contains(Author)").text().replace("Author: ", "");
        String releaseDate = container.select("p:contains(Release date)").text().replace("Release date: ", "").split("\\[")[0].trim();
        String language = container.select("p:contains(Language)").text().replace("Language: ", "");

        return new Metadata(title, author, releaseDate, language);
    }

    public List<String> extractChunks(String html) {
        List<String> paragraphs = extractParagraphs(html);
        List<String> chunks = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        for (String paragraph : paragraphs) {
            if (currentChunk.length() + paragraph.length() >= 500 && currentChunk.length() > 0) {
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
            }
            currentChunk.append(paragraph).append(" ");
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString());
        }

        return chunks;
    }
    
    public List<String> extractParagraphs(String html) {
        Document document = Jsoup.parse(html);
        Elements paragraphs = document.select("p");
        return paragraphs.stream()
                .map(Element::text)
                .collect(Collectors.toList());
    }
}
