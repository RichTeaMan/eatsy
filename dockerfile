#Multi-stage Build Dockerfile to ensure final image is smaller in size
#(leveraging the eatsyAppService-controller project layered jar defined in the controller gradle build file).

#The eatsyAppService-controller project jar is built in 4 layers (see it's gradle.build file).
#The decomposition of the application into different layers allows for the creation of an efficient Docker image.
#This improves efficiency and start up time of the docker image , especially if being run on limited infrastructure.

#Download all dependencies, and cache as a docker image layer (to reduce build time in future stages), build the jar (skip unit tests).
FROM gradle:7.3.3-jdk11 as cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY . /home/gradle/java-code/
WORKDIR /home/gradle/java-code
RUN gradle clean build -x test

#Extract the layers of the jar into folders/different layers
FROM adoptopenjdk:11-jre-hotspot as builder
COPY --from=cache /home/gradle/java-code/eatsyAppService/eatsyAppService-controller/build/libs/eatsyAppService-controller.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

#Finally, copy the extracted folders to add the corresponding Docker layers.
#With this configuration, when the src code is changed only the application layer will be rebuilt, the rest will remain cached.
#This leads to a more efficient and faster build process, especially when deploying the image to lightweight infrastructure
#Spring boots live application properties profile will be used.
FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder dependencies/ ./
COPY --from=builder module-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "-Dspring.profiles.active=live", "org.springframework.boot.loader.JarLauncher"]
