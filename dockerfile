#Dockerfile for deployment to Live on Render.com free tier web service.

#Eclipse Temurin build worthy JDK docker image
FROM eclipse-temurin:17-jdk-jammy as builder

#To define the working directory where the dockerfile commands will be run
WORKDIR /opt/app

#build up the JAR during the build process within the Dockerfile itself.
#The following RUN instructions trigger a goal that resolves all project dependencies, including plugins, reports, and their dependencies:

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
RUN ./gradlew --offline build
COPY ./src ./src
#build the project - run without tests (to reduce build time on Render.com free tier)
RUN ./gradlew clean build -x test


FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
#when the container starts using the application properties for Live deployment
ENTRYPOINT ["java", "-Dspring.profile.active=live", "-jar", "/opt/app/*.jar" ]