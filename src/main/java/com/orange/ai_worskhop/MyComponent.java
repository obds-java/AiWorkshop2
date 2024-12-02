package com.orange.ai_worskhop;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;

@RestController("/")
public class MyComponent {

    @Autowired
    private WeaviateClient client;

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
}