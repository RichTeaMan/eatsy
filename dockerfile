FROM gradle:7.3.3-jdk11 as cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle /home/gradle/java-code/
WORKDIR /home/gradle/java-code
RUN gradle clean build -x test

FROM adoptopenjdk:11-jre-hotspot as builder
COPY --from=cache /home/gradle/java-code/eatsyAppService/eatsyAppService-controller/build/libs/eatsyAppService-controller.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder dependencies/ ./
COPY --from=builder module-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "-Dspring.profile.active=live", "org.springframework.boot.loader.JarLauncher"]