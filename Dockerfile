FROM amazoncorretto:17.0.5 as build
WORKDIR /workspace/app

COPY . .
RUN ./gradlew build -x test

FROM amazoncorretto:17.0.5
ARG DEPENDENCY=/workspace/app/build/libs/
ARG JAR_FILE=BookReview-0.0.1-SNAPSHOT.jar
COPY --from=build ${DEPENDENCY}${JAR_FILE} /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
