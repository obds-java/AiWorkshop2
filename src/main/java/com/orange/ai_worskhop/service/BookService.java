package com.orange.ai_worskhop.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.orange.ai_worskhop.domain.Metadata;

@Service
public class BookService {
    
/**
     * Extracts metadata from the given HTML string.
     *
     * @param html the HTML content as a string
     * @return a Metadata object containing the extracted information
     */
    public Metadata extractMetadata(String html) {
        Document doc = Jsoup.parse(html);
        Metadata metadata = new Metadata();

        // Extract Title
        Element titleElement = doc.selectFirst("p:contains(Title) strong");
        if (titleElement != null) {
            // The text after the strong tag
            String titleText = titleElement.parent().ownText().trim();
            metadata.setTitle(titleText);
        }

        // Extract Author
        Element authorElement = doc.selectFirst("p:contains(Author) strong");
        if (authorElement != null) {
            String authorText = authorElement.parent().ownText().trim();
            metadata.setAuthor(authorText);
        }

        // Extract Release Date
        Element releaseDateElement = doc.selectFirst("p:contains(Release date) strong");
        if (releaseDateElement != null) {
            String releaseDateText = releaseDateElement.parent().ownText().split("\\[")[0].trim();
            metadata.setReleaseDate(releaseDateText);
        }

        // Extract Language
        Element languageElement = doc.selectFirst("p:contains(Language) strong");
        if (languageElement != null) {
            String languageText = languageElement.parent().ownText().trim();
            metadata.setLanguage(languageText);
        }

        return metadata;
    }

    /**
     * Splits an HTML text into chunks based on paragraph content.
     * Filters out invalid chunks and cleans up the text.
     * 
     * @param html the HTML content to split
     * @return List of valid text chunks
     */
    public List<String> chunkBook(String html) {
        Document doc = Jsoup.parse(html);
        List<String> chunks = new ArrayList<>();
        
        // Select all paragraph elements
        Elements paragraphs = doc.select("p");
        StringBuilder currentGroup = new StringBuilder();
        List<String> finalChunks = new ArrayList<>();

        // Process paragraphs
        for (Element p : paragraphs) {
            String text = p.text().trim();
            
            // Keep adding text to the current chunk (group) until it reaches 500 characters
            if (isValidChunk(text)) {
                if (currentGroup.length() + text.length() <= 500) {
                    if (currentGroup.length() > 0) {
                        currentGroup.append(" ");
                    }
                    currentGroup.append(text);
                } else {
                    if (currentGroup.length() > 0) {
                        finalChunks.add(currentGroup.toString());
                        currentGroup = new StringBuilder(text);
                    } else {
                        finalChunks.add(text);
                    }
                }
            }
        }

        // Add any remaining text
        if (currentGroup.length() > 0) {
            finalChunks.add(currentGroup.toString());
        }

        return finalChunks;
    }
    
    /**
     * Validates a text chunk to ensure it contains meaningful content.
     * 
     * @param text the chunk to validate
     * @return true if the chunk is valid
     */
    private boolean isValidChunk(String text) {
        // Skip empty or very short texts
        if (text == null || text.length() < 10) {
            return false;
        }
        
        // Skip chunks that are just formatting or contain no letters
        if (!text.matches(".*[a-zA-Z].*")) {
            return false;
        }
        
        // Skip chunks that are just HTML artifacts
        if (text.matches("^[\\p{Punct}\\s]+$")) {
            return false;
        }
        
        return true;
    }
}
