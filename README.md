# AiWorkshop2

## Commands

./mvnw clean install 
./mvnw compile jib:dockerBuild

docker-compose down --volumes --remove-orphans


## Basic premise

We are a library that is archiving all sorts of documents and making them queryable.
There are 2 main workflows:
1. Incoming document
The document is cleaned and split into chunks
The chunks are then converted into an embeddings
The embeddings are saved into the vector DB along with the original text and the meta information
2. Incoming search query
The query itself is converted into an embedding
A hybrid search is executed based on the keywords (or text search) and the vecotr search
The results are summarized by an LLM and returned to the user 

## First version
Simple vector search and embedding.

0. Create design: Requirements, data, APIs. Use o1 for brainstorming.
Data set: Project Gutenberg: https://www.gutenberg.org/browse/scores/top

1. Generate docker-compose w Weaviate.
`Help me get started with Weaviate in docker-compose.`
`Please explain to me the environment variables we're setting.`
`What does the transformers service do?` - SPILER ALERT: nothing, it's a hallucination

https://weaviate.io/developers/weaviate/installation/docker-compose
https://weaviate.io/developers/weaviate/model-providers/openai/embeddings

https://cookbook.openai.com/examples/vector_databases/weaviate/getting-started-with-weaviate-and-openai
https://github.com/openai/openai-cookbook/blob/main/examples/vector_databases/weaviate/docker-compose.yml

https://docs.spring.io/spring-ai/reference/api/vectordbs/weaviate.html

2. Add dependencies
`I'm integrating Weaviate into a Spring application with Maven. Can you help with that? Use the Spring starter modules where possible.`

# Requirements

* Java 21
* PlantUML
* GraphWiz


# Troubleshooting

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
      # TRANSFORMERS_INFERENCE_API: http://t2v-transformers:8080
      CLUSTER_HOSTNAME: 'node1'
    volumes:
      - ./data:./data

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
```...```
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

3. 