# =========================
# 1. Build stage
# =========================
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copy everything
COPY . .

# Build project
RUN gradle clean bootJar --no-daemon

# =========================
# 2. Runtime stage
# =========================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the final JAR from build
COPY --from=build /app/build/libs/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
