package com.orange.ai_worskhop.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.orange.ai_worskhop.domain.Book;
import com.orange.ai_worskhop.repository.VectorRepository;
import com.orange.ai_worskhop.service.BookService;
import com.orange.ai_worskhop.service.OpenAIService;

import lombok.extern.slf4j.Slf4j;

@RestController("/")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private VectorRepository vectorRepository;

    /**
     * Endpoint to upload a single HTML file, extract metadata, vectorize and save
     * the book.
     *
     * @param file the uploaded HTML file
     * @return ResponseEntity containing the extracted Metadata or an error message
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadHtmlFile(@RequestParam("file") MultipartFile file) {
        try {
            validateFile(file);
            String htmlContent = new String(file.getBytes());
            Book book = bookService.createBook(htmlContent);
            return ResponseEntity.ok(book);
        } catch (IOException e) {
            // Handle file read errors
            log.error("Error reading the uploaded file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading the uploaded file.");
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            log.error("Error processing the uploaded file", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle other errors
            log.error("Error processing the uploaded file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the file.");
        }
    }

    private void validateFile(MultipartFile file) {
        // Validate file is not empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please upload a non-empty HTML file.");
        }

        // Validate file type (optional)
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equalsIgnoreCase(MediaType.TEXT_HTML_VALUE)
                        && !contentType.equalsIgnoreCase("application/xhtml+xml"))) {
            throw new IllegalArgumentException("Please upload a valid HTML file.");
        }
    }

    /**
     * Endpoint to search for books by text similarity.
     *
     * @param text the text to search for
     * @return ResponseEntity containing the list of books or an error message
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchText(@RequestParam(name = "text") String text) {
        try {
            List<Book> books = vectorRepository.find(text);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            log.error("Error searching for books", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/generate")
    public String generate(@RequestParam(name = "prompt") String prompt) {
        return openAIService.generateText(prompt);
    }

    @GetMapping("/answer")
    public ResponseEntity<String> answerQuestion(@RequestParam(name = "question") String question) {
        try {
            List<Book> relevantBooks = vectorRepository.find(question);
            String context = relevantBooks.stream()
                    .flatMap((Book book) -> book.getChunks().stream())
                    .collect(Collectors.joining());

            String prompt = String.format("Based on the following context:\n%s\n\nAnswer this question: %s",
                    context, question);

            String answer = openAIService.generateText(prompt);
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            log.error("Error generating answer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating answer to question");
        }
    }

}
