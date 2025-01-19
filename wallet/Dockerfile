# Use an official Maven image to build the application
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Grant executable permissions to Maven wrapper script
RUN chmod +x ./mvnw

# Build the project and skip tests to create the JAR file
RUN ./mvnw clean package -DskipTests

# Use a lightweight Java runtime image for the final container
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory for the runtime container
WORKDIR /app

# Copy the JAR file from the build container
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
