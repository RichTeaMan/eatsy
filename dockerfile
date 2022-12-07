FROM adoptopenjdk:11-jre-hotspot as builder
ARG JAR_FILE=eatsyAppService/eatsyAppService-controller/build/libs/eatsyAppService.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder dependencies/ ./
COPY --from=builder module-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "-Dspring.profile.active=live", "org.springframework.boot.loader.JarLauncher"]