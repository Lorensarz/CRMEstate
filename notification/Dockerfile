FROM eclipse-temurin:21-jre-alpine
LABEL service.name="notification"
WORKDIR /app
COPY target/notification-1.0.0.jar app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]