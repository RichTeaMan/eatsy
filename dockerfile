#Dockerfile for deployment to Live on Render.com free tier web service.

FROM adoptopenjdk:11-jre-hotspot as builder
ARG JAR_FILE=build/*.jar
COPY ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "-Dspring.profile.active=live", "org.springframework.boot.loader.JarLauncher"]