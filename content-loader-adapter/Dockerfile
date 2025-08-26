FROM ghcr.io/graalvm/native-image-community:21-ol9 AS builder
RUN microdnf install maven
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests
RUN native-image -jar target/content-loader-adapter-*.jar --no-fallback -H:Name=content-loader-app
FROM oraclelinux:9-slim
WORKDIR /app
COPY --from=builder /app/content-loader-app ./app
COPY --from=builder /app/target/content-loader-adapter-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]