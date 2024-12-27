# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the local JAR file to the container
COPY ./target/todo-app-1.0-SNAPSHOT.jar myapp.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/myapp.jar"]
