# AiWorkshop2

This project is a RAG implementation that uses OpenAI models for embeddings and creating the answer.
A full solution is found on the solution branch, the main branch is used as a starting point for the workshop.

## Commands

 ### Building the project and the Docker image
./mvnw clean install
./mvnw compile jib:dockerBuild

### Deployment with docker-compose
docker-compose up
docker-compose down --volumes --remove-orphans

### Uploading a document
curl -X POST -F "file=@/<path_to_project>/AiWorkshop2/dataset/<book_name>.html" http://localhost:8080/upload

## Basic premise

We are a library that is archiving books and making them queryable.
There are 2 main workflows:
1. Uploading a book
The book is split into chunks and some metadata (title, author, etc.) is extracted
The chunks are then converted into an embeddings
The embeddings are saved into the vector DB along with the original text and the meta information
2. Querying the dataset (asking a question)
The query itself is converted into an embedding
Vector search is executed based on the search text. 
The Vector DB returns the K Nearest Neighbours (KNN) of the question vector 
The results are summarized by an LLM and returned to the user 

## Workshop agenda

0. Create design: Requirements, data, APIs. Use o1 for brainstorming.
Use data set: Project Gutenberg: https://www.gutenberg.org/browse/scores/top

1. Generate docker-compose w Weaviate.
`Help me get started with Weaviate in docker-compose.`
`Please explain to me the environment variables we're setting.`
`What does the transformers service do?` - SPILER ALERT: nothing, it was a hallucination

https://weaviate.io/developers/weaviate/installation/docker-compose
https://weaviate.io/developers/weaviate/model-providers/openai/embeddings

https://cookbook.openai.com/examples/vector_databases/weaviate/getting-started-with-weaviate-and-openai
https://github.com/openai/openai-cookbook/blob/main/examples/vector_databases/weaviate/docker-compose.yml

https://docs.spring.io/spring-ai/reference/api/vectordbs/weaviate.html

2. Add dependencies
`I'm integrating Weaviate into a Spring application with Maven. Can you help with that? Avoid using AI Spring starter modules.`

3. Create configuration class (it already generated this for me). Move the properties into the application.yml file.

4. Create the domain objects.

`Create the domain objects based on this plant uml diagram: <diagram_code>`

5. Create the API

`Generate the Spring Controller based on this API specification. Leave the methods empty, don't assume anything, just create the skeleton. <openapi_sepcification>`

6. Create BookService and start extracting the metadata.

Create a Spring service called BookService. For now, I only need one method in it. What it does is that it extracts the metadata from a html String and then returns it in a Metadata DTO. The html contains a large dom tree. The relevant information in looks like this:
```html
<div class="container" id="pg-machine-header"><p><strong>Title</strong>: A Doll's House : a play</p>
<div id="pg-header-authlist">
<p><strong>Author</strong>: Henrik Ibsen</p>
</div>
<p><strong>Release date</strong>: March 1, 2001 [eBook #2542]<br>
                Most recently updated: March 27, 2024</p>

<p><strong>Language</strong>: English</p>

<p><strong>Credits</strong>: Martin Adamson and David Widger</p>

</div><div id="pg-start-separator">
```

7. Create the chunking method. Discuss chunking techniques with a test time compute model, such as O1. 

`I need to split up the rest of the html into chunks. I'd like to extract each <p> element, add them to a list and return the list of Strings as a result.`

8. Optimize the chunking model

`Let's optimize this chunking method. Instead of using the content of each paragraph as is, I would like to make sure that each chunk is at least 500 characters long. I need the code to keep concatenating the paragraphs until they are this long and then add them as a chunk to the result list. Each paragraph should only appear in exactly one chunk. If a paragraph is already larger than 500 characters, the iteration can move on.`

Weaker models give a wrong answer here but Claude Sonnet should be able to do it. You can also try asking for a test first and then verify the solution with it.

9. Generate the OpenAI DTOs.

`Create the DTOs necessary for the service to call the endpoint described here:<br>https://platform.openai.com/docs/api-reference/chat/create`

This answer has a mistake in it, the request object was not generated correctly.

`The request is incorrect. Here is an example JSON, fix your mistake based on it.
  {
  "model": "gpt-4o-mini",
  "messages": [{"role": "user", "content": "Say this is a test!"}],
  "temperature": 0.7
  }
`

In case it gets the response wrong, here's an example for that:

```json
{
    "id": "chatcmpl-abc123",
    "object": "chat.completion",
    "created": 1677858242,
    "model": "gpt-4o-mini",
    "usage": {
        "prompt_tokens": 13,
        "completion_tokens": 7,
        "total_tokens": 20,
        "completion_tokens_details": {
            "reasoning_tokens": 0,
            "accepted_prediction_tokens": 0,
            "rejected_prediction_tokens": 0
        }
    },
    "choices": [
        {
            "message": {
                "role": "assistant",
                "content": "\n\nThis is a test!"
            },
            "logprobs": null,
            "finish_reason": "stop",
            "index": 0
        }
    ]
}
```

With this, it got it right but the solution wasn`t pretty enough.

`Ok this is perfect. Use this as basis and implement a solution using Java records.`

10. Generate the logic for calling the OpenAI chat endpoint.

`Ok, very cool. Now use the API documentation and these classes and generate a Spring Service that takes a String prompt and uses it as input for the chat completion endpoint. https://platform.openai.com/docs/api-reference/chat/create`

For me this turned out mostly good. It added too many dependencies but the service was correct.

11. Refactor the code to move the hyperparameters into application properties.

`This looks good. I'd like to put the hyperparameters such as temperature into the application.yml file.`

12. Finish the Controller class and fill in the remaining holes in the application.

`Use the controller class from earlier. The upload method endpoint will take a multipart file as input. It extracts the contents as a string and calls the BookService's createBook method with it. The answer endpoint takes a String as input and calls VectorRepository.find(input). It recieves a list of books as a response and iterates over them. It concatenates all the chunks in the book into a single String. It then calls the OpenAiService.generateText method and returns its response.`

This answer was pretty damn good. It was able to use all the information from the discussion so far and create a lot of correct code.

13. Manually fill in the remaining holes and test the application.

# Requirements

* Java 21
* Docker-compose
* OpenAI API key


# Troubleshooting examples

These examples come from my own experiences during the development process. They highlight how I personally use AI for sotfware development and can be used as inpiration.

1. I ran into an issue with docker-compose. I issued the following prompt in Microsoft Copilot.

----------------------------------

I got the following error. Can you help me solve it?

ERROR: for docker_weaviate_1  'ContainerConfig'

ERROR: for weaviate  'ContainerConfig'
Traceback (most recent call last):
  File "/usr/bin/docker-compose", line 33, in 
    sys.exit(load_entry_point('docker-compose==1.29.2', 'console_scripts', 'docker-compose')())
             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/cli/main.py", line 81, in main
    command_func()
  File "/usr/lib/python3/dist-packages/compose/cli/main.py", line 203, in perform_command
    handler(command, command_options)
  File "/usr/lib/python3/dist-packages/compose/metrics/decorator.py", line 18, in wrapper
    result = fn(*args, **kwargs)
             ^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/cli/main.py", line 1186, in up
    to_attach = up(False)
                ^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/cli/main.py", line 1166, in up
    return self.project.up(
           ^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/project.py", line 697, in up
    results, errors = parallel.parallel_execute(
                      ^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/parallel.py", line 108, in parallel_execute
    raise error_to_reraise
  File "/usr/lib/python3/dist-packages/compose/parallel.py", line 206, in producer
    result = func(obj)
             ^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/project.py", line 679, in do
    return service.execute_convergence_plan(
           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/service.py", line 579, in execute_convergence_plan
    return self._execute_convergence_recreate(
           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/service.py", line 499, in _execute_convergence_recreate
    containers, errors = parallel_execute(
                         ^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/parallel.py", line 108, in parallel_execute
    raise error_to_reraise
  File "/usr/lib/python3/dist-packages/compose/parallel.py", line 206, in producer
    result = func(obj)
             ^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/service.py", line 494, in recreate
    return self.recreate_container(
           ^^^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/service.py", line 612, in recreate_container
    new_container = self.create_container(
                    ^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/service.py", line 330, in create_container
    container_options = self._get_container_create_options(
                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/service.py", line 921, in _get_container_create_options
    container_options, override_options = self._build_container_volume_options(
                                          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/service.py", line 960, in _build_container_volume_options
    binds, affinity = merge_volume_bindings(
                      ^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/service.py", line 1548, in merge_volume_bindings
    old_volumes, old_mounts = get_container_data_volumes(
                              ^^^^^^^^^^^^^^^^^^^^^^^^^^^
  File "/usr/lib/python3/dist-packages/compose/service.py", line 1579, in get_container_data_volumes
    container.image_config['ContainerConfig'].get('Volumes') or {}
    ~~~~~~~~~~~~~~~~~~~~~~^^^^^^^^^^^^^^^^^^^
KeyError: 'ContainerConfig'

----------------------------------

This produces an incorrect, generic answer. 
I improved the prompt by adding the command and the actual docker-compose.yml to it.
I also ran it in the more powerful GPT 4o model.

----------------------------------

I am running into the following error after docker-compose up:
ERROR: for 527195cc11c4_docker_weaviate_1  'ContainerConfig'  ERROR: for weaviate  'ContainerConfig' Traceback (most recent call last):   File "/usr/bin/docker-compose", line 33, in <module>     sys.exit(load_entry_point('docker-compose==1.29.2', 'console_scripts', 'docker-compose')())              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/cli/main.py", line 81, in main     command_func()   File "/usr/lib/python3/dist-packages/compose/cli/main.py", line 203, in perform_command     handler(command, command_options)   File "/usr/lib/python3/dist-packages/compose/metrics/decorator.py", line 18, in wrapper     result = fn(*args, **kwargs)              ^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/cli/main.py", line 1186, in up     to_attach = up(False)                 ^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/cli/main.py", line 1166, in up     return self.project.up(            ^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/project.py", line 697, in up     results, errors = parallel.parallel_execute(                       ^^^^^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/parallel.py", line 108, in parallel_execute     raise error_to_reraise   File "/usr/lib/python3/dist-packages/compose/parallel.py", line 206, in producer     result = func(obj)              ^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/project.py", line 679, in do     return service.execute_convergence_plan(            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/service.py", line 579, in execute_convergence_plan     return self._execute_convergence_recreate(            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/service.py", line 499, in _execute_convergence_recreate     containers, errors = parallel_execute(                          ^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/parallel.py", line 108, in parallel_execute     raise error_to_reraise   File "/usr/lib/python3/dist-packages/compose/parallel.py", line 206, in producer     result = func(obj)              ^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/service.py", line 494, in recreate     return self.recreate_container(            ^^^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/service.py", line 612, in recreate_container     new_container = self.create_container(                     ^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/service.py", line 330, in create_container     container_options = self._get_container_create_options(                         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/service.py", line 921, in _get_container_create_options     container_options, override_options = self._build_container_volume_options(                                           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/service.py", line 960, in _build_container_volume_options     binds, affinity = merge_volume_bindings(                       ^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/service.py", line 1548, in merge_volume_bindings     old_volumes, old_mounts = get_container_data_volumes(                               ^^^^^^^^^^^^^^^^^^^^^^^^^^^   File "/usr/lib/python3/dist-packages/compose/service.py", line 1579, in get_container_data_volumes     container.image_config['ContainerConfig'].get('Volumes') or {}     ~~~~~~~~~~~~~~~~~~~~~~^^^^^^^^^^^^^^^^^^^ KeyError: 'ContainerConfig' 

Here is my docker-compose file: 
```
version: '3.4'

services:
  weaviate:
    image: semitechnologies/weaviate:latest
    ports:
      - "8080:8080"
    environment:
      QUERY_DEFAULTS_LIMIT: 20
      AUTHENTICATION_ANONYMOUS_ACCESS_ENABLED: 'true'
      PERSISTENCE_DATA_PATH: "./data"
      DEFAULT_VECTORIZER_MODULE: text2vec-openai
      ENABLE_MODULES: 'text2vec-openai,qna-openai'
      CLUSTER_HOSTNAME: 'node1'
    volumes:
      - ./data:./data
```

Can you help me solve it?

----------------------------------

This resulted in a much better answer. It explained that I am configuring the volumes incorrectly, why and how it would be correct.
Part of the answer was how I can prune the volumes. I issued those commands and I saw an orphan container. 
After cleaning the volumes and this orphan container up, I got the service up and running again.

2. I wasn't able to reach the application through REST. I used the following prompt: 

----------------------------------

I can't reach my Spring application under http://localhost:8081/ai-worskhop-2. Here's the application.yml:
```...```
The docker-compose.yml:
```
version: '3.4'

services:
  weaviate:
    image: semitechnologies/weaviate:latest
    ports:
      - "8081:8080"
    environment:
      QUERY_DEFAULTS_LIMIT: 20
      AUTHENTICATION_ANONYMOUS_ACCESS_ENABLED: 'true'
      PERSISTENCE_DATA_PATH: "/data"
      DEFAULT_VECTORIZER_MODULE: text2vec-openai
      ENABLE_MODULES: 'text2vec-openai,qna-openai'
      OPENAI_APIKEY: ''
      CLUSTER_HOSTNAME: 'node1'
    volumes:
      - ./data:/data
  ai-workshop:
    image: ai-workshop:latest
    ports:
      - "8081:8081"
```
And the Controller:
```...```
What could be the problem?

----------------------------------

It correctly pointed to the fact that I was mapping the incorrect internal port in docker-compose, I used 
  ai-workshop:
    image: ai-workshop:latest
    ports:
      - "8081:8081"
Even though the service inside is running on 8080.

3. Since I couldn't find a good OpenAI clinet written in Java (and not for Spring), I wanted to ask it to generate it for me.

I want to send a request to OpenAI from my Spring service. I need you to create the client class that does this. Don't use the Spring AI library.
Here is a description of the API I'd like to call:
https://platform.openai.com/docs/api-reference/chat/create

It has generated the Spring service using the web client of Spring and the DTOs. It made a small mistake with the DTOs. 
I asked it for a correction and pasted the example JSON from the documentation. It correctly generated the code. 