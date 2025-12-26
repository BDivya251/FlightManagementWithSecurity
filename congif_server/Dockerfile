#FROM maven:3.9.9-eclipse-temurin-21 AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*SNAPSHOT.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
