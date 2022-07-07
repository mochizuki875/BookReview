# BookReview
BookReview is Spring Boot Web Application that provides a service for posting book information and reviews.

## Prerequisites

```
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode)
org.springframework.boot 2.6.7
```

## Run

```
# ./gradlew bootRun
```

or

```
# ./gradlew build
# java -jar build/libs/BookReview-0.0.1-SNAPSHOT.jar
```

Access `http://127.0.0.1:8080`.
You can access H2 Console `http://127.0.0.1:8080/h2-console/` with below params.

|Name         |Value                   |
|:---         |:---                    |
|Driver Class |`org.h2.Driver`         |
|JDBC URL     |`jdbc:h2:mem:bookreview`|
|User Name    |`sa`                    |
|Password     |                        |


### Docker

```
# docker build -t bookreview:1.0 .
# docker run -d -p 8080:8080 --name bookreview bookreview:v1.0
```


## Configuration

|Name                        |Description                                                                                   |Value|
|:---                        |:---                                                                                          |:---|
|`buildEnv`                  |If you set `prod`, using database(`PostgreSQL 14.x`) as application datastore.(Default: `dev`)|`dev`/`prod`|
|`SPRING_DATASOURCE_URL`     |Set database URL.(Default: `jdbc:postgresql://localhost:5432/bookreview`)                     |`jdbc:postgresql://<Host Name>:<Port>/<Database Name>`|
|`SPRING_DATASOURCE_USERNAME`|Set database user name.(Default: `postgres`)                                                  |`<USERNAME>`|
|`SPRING_DATASOURCE_PASSWORD`|Set database user password.(Default: `postgres`)                                              |`<PASSWORD>`|
|`JAVA_LOG_LEVEL`            |Set log level.(Default: `INFO`)                                                               |`TRACE`/`DEBUG`/`INFO`/`WARN`/`ERROR`/`FATAL`/`OFF`|
