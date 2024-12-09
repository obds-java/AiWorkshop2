package com.orange.ai_worskhop.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
}
