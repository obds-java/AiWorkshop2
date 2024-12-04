package com.orange.ai_worskhop.domain;

public class Metadata {
    private String title;
    private String author;
    private String releaseDate;
    private String language;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
