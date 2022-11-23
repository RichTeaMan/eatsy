FROM openjdk:8-jdk-alpine

RUN mkdir eatsy

#Download repo as tar file, extract all and remove the parent folder to put us in the project root
RUN  curl -L https://github.com/DM1st/eatsy/archive/flyio.tar.gz | tar -xz --strip-components=1

RUN ./gradlew build

EXPOSE 8080

ENTRYPOINT ./gradlew bootRun