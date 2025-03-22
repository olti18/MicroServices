# Start with a base image containing Java runtime
FROM openjdk:23-jdk-slim

# Information about the maintainer
LABEL org.opencontainers.image.authors="Olti Berisha"

# Install curl for healthcheck support
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Add the application's jar to the image
COPY target/configserver-0.0.1-SNAPSHOT.jar configserver-0.0.1-SNAPSHOT.jar

# Execute the application
ENTRYPOINT ["java", "-jar", "configserver-0.0.1-SNAPSHOT.jar"]







##Start with a base image containing Java runtime
#FROM openjdk:23-jdk-slim
#
## MAINTAINER Olti Berisha
##Information around who maintains the image
#LABEL "org.opencontainers.image.authors"="Olti Berisha"
#
## Add the application's jar to the image
#COPY target/configserver-0.0.1-SNAPSHOT.jar configserver-0.0.1-SNAPSHOT.jar
#
## execute the application
#ENTRYPOINT ["java", "-jar", "configserver-0.0.1-SNAPSHOT.jar"]
#
#RUN apt-get update && apt-get install -y curl
