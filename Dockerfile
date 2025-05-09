FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app


COPY pom.xml .
RUN mvn dependency:go-offline


COPY src/ /app/src/
RUN mvn package -DskipTests


FROM openjdk:17-jdk-slim
WORKDIR /app


COPY --from=build /app/target/*.jar app.jar


EXPOSE 8080

ENV SPRING_DATASOURCE_URL=jdbc:mysql://db:8089/mindful8089
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=password
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]