# BookReview
BookReview is Spring Boot Web Application that provides a service for posting book information and reviews.

## Prerequisites

```
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode)
org.springframework.boot 2.6.7
```

## Quick Start
Run Book Review application using in-memory database `H2`.

```
# ./gradlew bootRun
```

Alternatively, you can run using docker.

```
# docker run -d -p 8080:8080 --name bookreview ghcr.io/mochizuki875/bookreview:latest
```

Access `http://127.0.0.1:8080`.


## Configuration

|Name                        |Description                                                                                   |Value|
|:---                        |:---                                                                                          |:---|
|`buildEnv`                  |If you set `prod`, using database(`PostgreSQL 14.x`) as application datastore.(Default: `dev`)|`dev`/`prod`|
|`SPRING_DATASOURCE_URL`     |Set database URL.(Default: `jdbc:postgresql://localhost:5432/bookreview`)                     |`jdbc:postgresql://<Host Name>:<Port>/bookreview`|
|`SPRING_DATASOURCE_USERNAME`|Set database user name.(Default: `postgres`)                                                  |`<USERNAME>`|
|`SPRING_DATASOURCE_PASSWORD`|Set database user password.(Default: `postgres`)                                              |`<PASSWORD>`|
|`JAVA_LOG_LEVEL`            |Set log level.(Default: `INFO`)                                                                |`TRACE`/`DEBUG`/`INFO`/`WARN`/`ERROR`/`FATAL`/`OFF`|

## Run

```
# ./gradlew bootRun
```

or

```
# ./gradlew build
# java -jar build/libs/BookReview-0.0.1-SNAPSHOT.jar
```

## Docker

```
# docker build -t bookreview:1.0 .
# docker run -d -p 8080:8080 --name bookreview bookreview:1.0
```