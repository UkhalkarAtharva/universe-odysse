# ==============================================================================
# Stage 1: Build the Application
# ==============================================================================
FROM maven:3.9-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies (for caching)
COPY pom.xml .
# Download dependencies without building to cache them
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application (skip tests for faster production builds)
RUN mvn clean package -DskipTests

# ==============================================================================
# Stage 2: Create the Runtime Image
# ==============================================================================
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Configure JVM options for container environments
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Command to run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
