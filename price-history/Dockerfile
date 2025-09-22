FROM eclipse-temurin:21-jre-alpine
LABEL service.name="price-history"
WORKDIR /app
COPY target/price-history-1.0.0.jar app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]