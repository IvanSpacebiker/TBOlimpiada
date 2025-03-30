FROM gradle:8.5-jdk21-alpine AS build
COPY --chown=gradle:gradle . /home/app
WORKDIR /home/app
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine-3.21
EXPOSE 10000
COPY --from=build /home/app/build/libs/tbolimpiada-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Xmx1024M", "-XX:MaxMetaspaceSize=1024M", "-jar", "app.jar"]
