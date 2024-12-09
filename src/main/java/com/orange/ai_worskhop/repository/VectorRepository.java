package com.orange.ai_worskhop.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orange.ai_worskhop.domain.Book;
import com.orange.ai_worskhop.domain.Metadata;

import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearTextArgument;
import io.weaviate.client.v1.graphql.query.builder.GetBuilder;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.graphql.query.fields.Fields;

@Service
public class VectorRepository {

  private static final String CLASS_NAME = "Paragraph";
  private static final int SEARCH_LIMIT = 10;

  @Autowired
  private WeaviateClient client;

  public Book saveBook(Book book) {
    // Iterate the chunks of the book paralelly
    book.chunks().parallelStream().forEach(chunk -> {
      client.data().creator()
          .withClassName(CLASS_NAME)
          .withID(UUID.randomUUID().toString())
          .withProperties(
              Map.of(
                  "title", book.metadata().title(),
                  "author", book.metadata().author(),
                  "releaseDate", book.metadata().releaseDate(),
                  "language", book.metadata().language(),
                  "chunk", chunk))
          .withConsistencyLevel(ConsistencyLevel.QUORUM)
          .run();
    });
    return book;
  }

  /**
   * https://weaviate.io/developers/weaviate/search/similarity
   */
  public List<Book> find(String text) {
    String query = createFindQuery(text);

    Result<GraphQLResponse> result = client.graphQL().raw().withQuery(query).run();
    Map<Metadata, Book> bookMap = processResult(result);

    return new ArrayList<>(bookMap.values());
  }

  private String createFindQuery(String text) {
    NearTextArgument nearText = NearTextArgument.builder()
        .concepts(new String[] { text })
        .build();

    Fields fields = Fields.builder()
        .fields(new Field[] {
            Field.builder().name("title").build(),
            Field.builder().name("author").build(),
            Field.builder().name("releaseDate").build(),
            Field.builder().name("language").build(),
            Field.builder().name("chunk").build(),
            Field.builder().name("_additional").fields(new Field[] {
                Field.builder().name("distance").build()
            }).build()
        })
        .build();

    String query = GetBuilder.builder()
        .className(CLASS_NAME)
        .fields(fields)
        .withNearTextFilter(nearText)
        .limit(SEARCH_LIMIT)
        .build()
        .buildQuery();
    return query;
  }

  private Map<Metadata, Book> processResult(Result<GraphQLResponse> result) {
    Map data = (Map<String, Map>) result.getResult().getData();

    Map get = (Map) data.get("Get");
    List<Map> paragraphs = (List) get.get(CLASS_NAME);

    Map<Metadata, Book> bookMap = new HashMap<>();

    for (Map paragraph : paragraphs) {
      Object title = paragraph.get("title");
      Object author = paragraph.get("author");
      Object releaseDate = paragraph.get("releaseDate");
      Object language = paragraph.get("language");
      Object chunk = paragraph.get("chunk");
      Object distance = ((Map) paragraph.get("_additional")).get("distance");

      Metadata metadata = new Metadata(
          title.toString(),
          author.toString(),
          releaseDate.toString(),
          language.toString());

      Book book = bookMap.computeIfAbsent(metadata, k -> new Book(metadata, new ArrayList<>()));
      book.chunks().add(chunk.toString());
    }
    return bookMap;
  }
}