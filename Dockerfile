FROM openjdk:17-jdk-slim-buster
EXPOSE 8080
ADD target/cloudService-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java", "-jar", "/myapp.jar"]