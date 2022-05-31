FROM openjdk:11 as build
WORKDIR /workspace/app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN ./gradlew build -x test

FROM openjdk:11-jre-slim
ARG DEPENDENCY=/workspace/app/build/libs/
ARG JAR_FILE=BookReview-0.0.1-SNAPSHOT.jar
COPY --from=build ${DEPENDENCY}${JAR_FILE} /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
