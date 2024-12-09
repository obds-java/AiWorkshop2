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
import com.orange.ai_worskhop.repository.VectorRepository;
import com.orange.ai_worskhop.service.BookService;
import com.orange.ai_worskhop.service.OpenAiService;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    OpenAiService openAiService;

    @Autowired
    VectorRepository vectorRepository;

    @PostMapping("/upload")
    public Book uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String htmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            Book book = bookService.createBookFromHtml(htmlContent);
            vectorRepository.saveBook(book);
            return book;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam("text") String text) {
        return vectorRepository.find(text);
    }

    @GetMapping("/generate")
    public String generateText(@RequestParam("prompt") String prompt) {
        // Method body not included
        return null;
    }

    @GetMapping("/answer")
    public String answerQuestion(@RequestParam("question") String question) {
        List<Book> books = searchBooks(question);

        StringBuilder context = new StringBuilder();
        for (Book book : books) {
            context.append(String.join(" ", book.chunks()));
            context.append("\n");
        }
        question += context.toString() + "\nAnswer the following question only using data from the context above. Question: " + question;

        return openAiService.getChatCompletion(question);
    }
}
