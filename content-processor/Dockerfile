FROM eclipse-temurin:21-jre-alpine
LABEL service.name="content-processor"
WORKDIR /app
COPY target/*.jar app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]