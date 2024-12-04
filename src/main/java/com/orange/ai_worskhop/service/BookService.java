package com.orange.ai_worskhop.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

    public void chunkBook() {

    }
}
