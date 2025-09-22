FROM eclipse-temurin:21-jre-alpine
LABEL service.name="core-crm"
WORKDIR /app
COPY target/core-crm-1.0.0.jar app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]