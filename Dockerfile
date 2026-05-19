# ── Stage 1: Build ───────────────────────────────────────────────────────────
FROM gradle:8-jdk21 AS build
 
WORKDIR /workspace
 
# Copy Gradle wrapper and root build files
COPY gradlew .
COPY gradle/ gradle/
COPY settings.gradle* .
COPY build.gradle* .
 
# Copy all modules (common is included as core/worker depend on it)
COPY common/build.gradle* common/
COPY common/src common/src
COPY core/build.gradle* core/
COPY core/src core/src
COPY worker/build.gradle* worker/
COPY worker/src worker/src
 
# Build all modules (skipping tests for speed)
RUN ./gradlew bootJar --no-daemon -x test
 
# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
 
WORKDIR /app
 
# MODULE build arg tells us which module's jar to use (e.g. "core" or "worker")
ARG MODULE
COPY --from=build /workspace/${MODULE}/build/libs/*.jar app.jar
 
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
