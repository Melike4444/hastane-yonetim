# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Maven wrapper + pom
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

# Source
COPY src src

# Package (jar)
RUN ./mvnw -q -DskipTests package

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
