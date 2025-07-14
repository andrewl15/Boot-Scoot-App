FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "/app.jar"]
