FROM openjdk:8-jdk-alpine

#Use 'ADD [source URL] [destination file]' to download repo as tar file.
ADD  https://github.com/DM1st/eatsy/archive/flyio.tar.gz eatsy.tar.gz
RUN ls
#Create a folder to store the project
run mkdir eatsy

#Extract tar file and remove the parent folder (to put us in the project root)
#Specify to extract to the newly created eatsy directory
RUN tar -xf eatsy.tar.gz --strip-components=1 -C eatsy
RUN ls
WORKDIR /eatsy
RUN ./gradlew build

EXPOSE 8080

ENTRYPOINT ./gradlew bootRun