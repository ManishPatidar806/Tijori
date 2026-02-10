# Tijori Admin Server - Dockerfile
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
COPY src ./src

# Build the application
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests -B && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

# Create non-root user
RUN groupadd -r admingroup && useradd -r -g admingroup adminuser

WORKDIR /app

# Copy the built JAR
COPY --from=build /app/target/Tijori-Admin-Server-1.0.0.jar app.jar

# Change ownership
RUN chown -R adminuser:admingroup /app

# Switch to non-root user
USER adminuser

EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

# JVM optimization
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]

