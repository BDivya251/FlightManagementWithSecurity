FROM eclipse-temurin:21

WORKDIR /app
COPY target/*.jar config-server.jar

ENTRYPOINT ["java", "-jar", "config-server.jar"]
