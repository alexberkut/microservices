FROM gradle:jdk17-alpine AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY resource-service/build.gradle ./resource-service/build.gradle
COPY song-service/build.gradle ./song-service/build.gradle

RUN ./gradlew dependencies --no-daemon

COPY song-service/src ./song-service/src

RUN ./gradlew :song-service:assemble --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/song-service/build/libs/*.jar app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]
