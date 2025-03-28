# Use the official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /tmp

# Copy the Maven build file and the source code to the container
COPY target/ManageDevices-0.0.1-SNAPSHOT.jar device-management.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "device-management.jar"]