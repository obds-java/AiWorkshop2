package com.orange.ai_worskhop.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.orange.ai_worskhop.domain.Book;
import com.orange.ai_worskhop.service.BookService;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/upload")
    public Book uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String htmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            return bookService.createBookFromHtml(htmlContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam("text") String text) {
        // Method body not included
        return null;
    }

    @GetMapping("/generate")
    public String generateText(@RequestParam("prompt") String prompt) {
        // Method body not included
        return null;
    }

    @GetMapping("/answer")
    public String answerQuestion(@RequestParam("question") String question) {
        // Method body not included
        return null;
    }
}
