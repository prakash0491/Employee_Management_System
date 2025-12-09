# ---------- 1. Build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# copy project files
COPY pom.xml .
COPY src ./src

# build, skip tests (since tests may fail without DB setup)
RUN mvn clean package -DskipTests

# ---------- 2. Run stage ----------
FROM openjdk:17-jdk-slim
WORKDIR /app

# copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# expose port (Render sets PORT; Spring Boot defaults to 8080)
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
