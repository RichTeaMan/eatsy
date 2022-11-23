FROM openjdk:8-jdk-alpine

RUN mkdir eatsy

#Use ADD to download repo as tar file, extract all and remove the parent folder to put us in the project root
#ADD [source] [destination]
ADD  https://github.com/DM1st/eatsy/archive/flyio.tar.gz eatsy
RUN ls
WORKDIR /eatsy
RUN ls
RUN ./gradlew build

EXPOSE 8080

ENTRYPOINT ./gradlew bootRun