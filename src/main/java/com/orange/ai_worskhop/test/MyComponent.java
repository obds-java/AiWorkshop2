package com.orange.ai_worskhop.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.orange.ai_worskhop.domain.Metadata;
import com.orange.ai_worskhop.service.BookService;

import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearTextArgument;
import io.weaviate.client.v1.graphql.query.builder.GetBuilder;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.graphql.query.fields.Fields;

@RestController("/")
public class MyComponent {

    @Autowired
    private WeaviateClient client;

    @Autowired
    private BookService bookService;

    @GetMapping("add")
    public void insertTestData() {

    Result<WeaviateObject> result1 = client.data().creator()
      .withClassName("Book")
      .withID("44985632-4fbc-4529-9a1f-42cdcba846d1")
      .withProperties(Map.of("title", "The Lord of the Rings", "author", "J. R. R. Tolkien"))
      .withConsistencyLevel(ConsistencyLevel.QUORUM)
      .run();
        System.out.println(result1.getResult());

        Result<WeaviateObject> result2 = client.data().creator()
      .withClassName("Book")
      .withID("f5d5a6d0-6e3e-4b2a-9d8f-1a0d7b8f6b1f")
      .withProperties(Map.of("title", "Game of Thrones", "author", "George R. R. Martin"))
      .withConsistencyLevel(ConsistencyLevel.QUORUM)
      .run();
System.out.println(result2.getResult());

      Result<WeaviateObject> result3 = client.data().creator()
      .withClassName("Book")
      .withID("b3f8b8d7-3f50-4d14-8e6d-0c7b0a0b9e8f")
      .withProperties(Map.of("title", "Harry Potter", "author", "J. K. Rowling"))
      .withConsistencyLevel(ConsistencyLevel.QUORUM)
      .run();
System.out.println(result3.getResult());

      Result<WeaviateObject> result4 = client.data().creator()
      .withClassName("Book")
      .withID("f1b6e0b2-0f26-4f1c-9f9d-3b7a2a0c7a2e")
      .withProperties(Map.of("title", "A knight of the seven kingdoms", "author", "George R. R. Martin"))
      .withConsistencyLevel(ConsistencyLevel.QUORUM)
      .run();
    System.out.println(result4.getResult());
    }

    @GetMapping("get")
    public List<WeaviateObject> get() {
        Result<List<WeaviateObject>> objsAdditionalT = client.data()
        .objectsGetter()
        // .withID("44985632-4fbc-4529-9a1f-42cdcba846d1")
        .withClassName("Book")
        // .withAdditional("classification")
        // .withAdditional("nearestNeighbors")
        .withVector()
        .run();
        return objsAdditionalT.getResult();
    }

    @GetMapping("find")
    public List<String> find(@RequestParam(name = "text") String text) {
      List<String> response = new ArrayList<String>();

      NearTextArgument nearText = NearTextArgument.builder()
        .concepts(new String[]{ text })
        .build();

      Fields fields = Fields.builder()
        .fields(new Field[]{
          Field.builder().name("title").build(),
          Field.builder().name("author").build(),
          Field.builder().name("_additional").fields(new Field[]{
            Field.builder().name("distance").build()
          }).build()
        })
        .build();

      String query = GetBuilder.builder()
        .className("Book")
        .fields(fields)
        .withNearTextFilter(nearText)
        .limit(2)
        .build()
        .buildQuery();

      Result<GraphQLResponse> result = client.graphQL().raw().withQuery(query).run();
      Map data = (Map<String, Map>)result.getResult().getData();
      Map get = (Map)data.get("Get");
      List<Map> books = (List)get.get("Book");
      for (Map book : books) {
        Object author = book.get("author");
        Object title = book.get("title");
        Object distance = ((Map)book.get("_additional")).get("distance");
        response.add("Author: " + author + "; Title: " + title + "; Distance: " + distance);
      }

      return response;
    }

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

            // Return the metadata as JSON
            return ResponseEntity.ok(metadata);
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
}