FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /home/app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /opt/app

COPY --from=build /home/app/target/*.jar ./*.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar","/opt/app/*.jar"]