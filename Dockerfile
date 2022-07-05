FROM openjdk:11 as build
WORKDIR /workspace/app

COPY . .
RUN ./gradlew build -x test

FROM openjdk:11-jre-slim
ARG DEPENDENCY=/workspace/app/build/libs/
ARG OTEL=/workspace/app/otel/
ARG JAR_FILE=*.jar
COPY --from=build ${DEPENDENCY}${JAR_FILE} /app.jar
COPY --from=build ${OTEL}${JAR_FILE} /otel.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
