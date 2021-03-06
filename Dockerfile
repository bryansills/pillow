FROM openjdk:11 AS BUILD_JAR
WORKDIR /usr/app/
COPY . .
RUN ./gradlew clean :server:build --no-daemon --stacktrace

FROM openjdk:11-jre
RUN mkdir /app
COPY --from=BUILD_JAR /usr/app/server/build/libs/pillow.jar /app/pillow.jar
WORKDIR /app
CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "pillow.jar"]
