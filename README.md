# queuedvthreads

This project aims to provide multi-threaded queued task consuming experience via virtual threads

Project contains
  - RabbitMQ publisher
  - RabbitMQ consumer
  - Virtual Thread Manager
  - Sample runnable task

Scenario
  - A message comes from another service
  - The controller sends messages to the service to save it in the database and then publish it to the queue
  - Consumer reads the message from queue
  - If the message is not processed successfully, it goes to the retry queue where it is tried again with the delay specified by the user in the application.yml file
  - If the message is not processed after the number of retries it goes to the unprocessed queue
  - Retry limit is also specified in the application.yml file

Tech
  - RabbitMQ
  - Spring Boot
  - H2 DB
  - Docker
