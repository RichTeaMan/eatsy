FROM openjdk:8-jdk-alpine

RUN mkdir project

#Use 'ADD [source URL] [destination folder]' to download repo as tar file.
ADD  https://github.com/DM1st/eatsy/archive/flyio.tar.gz /project/eatsy
RUN ls
#swtich into the working directory
WORKDIR /eatsy
run ls
#extract tar file and remove the parent folder (to put us in the project root)
RUN tar -x flyio.tar.gz --strip-components=1
RUN ls
RUN ./gradlew build

EXPOSE 8080

ENTRYPOINT ./gradlew bootRun