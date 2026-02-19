FROM gradle:jdk17-alpine AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY resource-service/build.gradle ./resource-service/build.gradle
COPY song-service/build.gradle ./song-service/build.gradle

RUN ./gradlew dependencies --no-daemon

COPY resource-service/src ./resource-service/src

RUN ./gradlew :resource-service:assemble --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/resource-service/build/libs/*.jar app.jar
EXPOSE 8081
EXPOSE 5005

CMD ["java", "-jar", "app.jar"]
