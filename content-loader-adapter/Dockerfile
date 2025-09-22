FROM eclipse-temurin:21-jre-alpine
LABEL service.name="content-loader-adapter"
WORKDIR /app
COPY target/content-loader-adapter-1.0.0.jar app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]