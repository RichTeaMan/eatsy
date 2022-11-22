FROM openjdk:8-jdk-alpine

RUN mkdir eatsy
ADD https://github.com/DM1st/eatsy/archive/refs/heads/flyio.zip eatsy.tar.gz
RUN tar -xzf eatsy.tar.gz --strip-components=1 -C eatsy
WORKDIR /eatsy
RUN ./gradlew build

EXPOSE 8080

ENTRYPOINT ./gradlew bootRun