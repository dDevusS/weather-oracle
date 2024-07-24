FROM gradle:7-jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon -x test

FROM openjdk:17.0.2-slim
WORKDIR /app
COPY --from=build /app/build/libs/weather-oracle-1.0.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]