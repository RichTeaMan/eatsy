#openJDK docker image
FROM openjdk:8-jdk-alpine

#Use ADD '[source URL] [destination file]' to download repo as tar file.
ADD  https://github.com/DM1st/eatsy/archive/flyio.tar.gz eatsy.tar.gz

#Create a folder to store the project
run mkdir eatsy

#Specify archive file and extract tar file and remove the parent folder (to put us in the project root)
#Specify to extract to the newly created eatsy directory
RUN tar -xf eatsy.tar.gz --strip-components=1 -C eatsy

#Switch to the eatsy directory
WORKDIR /eatsy

#Set Environment variables for deployed Postgres instance on Render.com
ENV SPRING_DATASOURCE_URL postgres://test:agQSxtwNA1Yi5v5yLcQ8yQInkcYrHkco@dpg-ce0d38g2i3mkuccq1n70-a/eatsy
ENV SPRING_DATASOURCE_USERNAME test
ENV SPRING_DATASOURCE_PASSWORD agQSxtwNA1Yi5v5yLcQ8yQInkcYrHkco
ENV SPRING_JPA_HIBERNATE_DDL_AUTO update

#build the project - run without tests (due to not having a specified database established)
RUN ./gradlew build -x test

#Informs Docker the container listens on this port at runtime
EXPOSE 8080

#Configures the bootRun command to run when the container starts.
ENTRYPOINT ./gradlew bootRun