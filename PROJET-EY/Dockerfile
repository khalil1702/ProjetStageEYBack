# Stage 1: Build the app (optional if you already have the JAR)
# FROM maven:3.9.6-eclipse-temurin-17 AS build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package -DskipTests

# Stage 2: Run the Spring Boot JAR
FROM eclipse-temurin:17-jdk-jammy

# Create app directory
WORKDIR /app

# Copy the built JAR into the container
COPY target/projetStage-0.0.1-SNAPSHOT.jar app.jar

# Copy custom application.properties into the container (if needed)
# COPY src/main/resources/application.properties /app/config/application.properties

# Expose the Spring Boot port
EXPOSE 8089

# Default command
ENTRYPOINT ["java", "-jar", "app.jar"]
