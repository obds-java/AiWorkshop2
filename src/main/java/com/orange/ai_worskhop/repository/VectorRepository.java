package com.orange.ai_worskhop.repository;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orange.ai_worskhop.domain.Book;

import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearTextArgument;
import io.weaviate.client.v1.graphql.query.builder.GetBuilder;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.graphql.query.fields.Fields;

@Service
public class VectorRepository {

    private static final String CLASS_NAME = "Paragraph";
    @Autowired
    private WeaviateClient client;

    public void saveBook(Book book) {

      // Iterate the chunks of the book paralelly
      book.getChunks().parallelStream().forEach(chunk -> {
        Result<WeaviateObject> result = client.data().creator()
        .withClassName(CLASS_NAME)
        .withID(UUID.randomUUID().toString())
        .withProperties(
          Map.of(
          "title", book.getMetadata().getTitle(),
          "author", book.getMetadata().getAuthor(),
          "releaseDate", book.getMetadata().getReleaseDate(),
          "language", book.getMetadata().getLanguage(),
          "chunk", chunk
          ))
        .withConsistencyLevel(ConsistencyLevel.QUORUM)
        .run();
        System.out.println(result.getResult());
      });
    }

    /**
     * https://weaviate.io/developers/weaviate/search/similarity
     */
    public Book find(String text) {
      NearTextArgument nearText = NearTextArgument.builder()
        .concepts(new String[]{ text })
        .build();

        Fields fields = Fields.builder()
        .fields(new Field[]{
          Field.builder().name("title").build(),
          Field.builder().name("author").build(),
          Field.builder().name("releaseDate").build(),
          Field.builder().name("language").build(),
          Field.builder().name("chunk").build(),
          Field.builder().name("_additional").fields(new Field[]{
            Field.builder().name("distance").build()
          }).build()
        })
        .build();

      String query = GetBuilder.builder()
        .className(CLASS_NAME)
        .fields(fields)
        .withNearTextFilter(nearText)
        .limit(2)
        .build()
        .buildQuery();

      Result<GraphQLResponse> result = client.graphQL().raw().withQuery(query).run();
      Object data = result.getResult().getData();
      System.out.println(data);

      return null;
    }
}