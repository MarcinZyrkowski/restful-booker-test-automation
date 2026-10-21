# Use an official JDK 21 image as the base image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew ./
COPY gradle/ ./gradle/

# Give execution rights on the Gradle wrapper
RUN chmod +x ./gradlew

# Copy the build configuration files
COPY build.gradle settings.gradle ./

# Optional: Download dependencies in a separate layer to cache them
# This will speed up subsequent builds if dependencies don't change
RUN ./gradlew dependencies --no-daemon > /dev/null 2>&1 || true

# Copy the rest of the project source code
COPY src/ ./src/
# Copying GEMINI.md or README.md in case they are referenced somewhere
COPY GEMINI.md README.md ./

# Define the command to run the tests
# Users can override this command when running the container
ENTRYPOINT ["./gradlew", "test", "--no-daemon"]
