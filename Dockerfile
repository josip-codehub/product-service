FROM gradle:8.12-jdk17-jammy AS build
ENV ENV_DATASOURCE_URL, ENV_DATASOURCE_USERNAME, ENV_DATASOURCE_PASSWORD
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle build -x test --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/products-service-*.jar app.jar
EXPOSE 8080
EXPOSE 9090

ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-jar", "app.jar"]
