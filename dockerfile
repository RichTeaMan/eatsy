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

#build the project - run without tests
RUN ./gradlew build -x test

#Informs Docker the container listens on this port at runtime
EXPOSE 8080

#Configures the bootRun command to run when the container starts.
ENTRYPOINT ./gradlew bootRun