package com.orange.ai_worskhop.controller;

import java.io.IOException;
import java.util.List;

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
import com.orange.ai_worskhop.domain.Metadata;
import com.orange.ai_worskhop.repository.VectorRepository;
import com.orange.ai_worskhop.service.BookService;

@RestController("/")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private VectorRepository vectorRepository;

    /**
     * Endpoint to upload a single HTML file and extract metadata.
     *
     * @param file the uploaded HTML file
     * @return ResponseEntity containing the extracted Metadata or an error message
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadHtmlFile(@RequestParam("file") MultipartFile file) {
        // Validate file is not empty
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please upload a non-empty HTML file.");
        }

        // Validate file type (optional)
        String contentType = file.getContentType();
        if (contentType == null || 
            (!contentType.equalsIgnoreCase(MediaType.TEXT_HTML_VALUE) 
             && !contentType.equalsIgnoreCase("application/xhtml+xml"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please upload a valid HTML file.");
        }

        try {
            // Read the file content as a string
            String htmlContent = new String(file.getBytes());

            // Extract metadata
            Metadata metadata = bookService.extractMetadata(htmlContent);

            // Chunk book
            List<String> chunks = bookService.chunkBook(htmlContent);

            // Create Book
            Book book = new Book(metadata, chunks);

            // Save book to Weaviate
            vectorRepository.saveBook(book);

            // Return the metadata as JSON
            return ResponseEntity.ok(book);
        } catch (IOException e) {
            // Handle file read errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading the uploaded file.");
        } catch (Exception e) {
            // Handle other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the file.");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchText(@RequestParam String text) {
        try {
            vectorRepository.find(text);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
}  