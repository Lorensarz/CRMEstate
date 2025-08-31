FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/content-processor-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]