# Eatsy App Service

A REST API for creating, viewing, editing and deleting your favourite recipes.

[![CircleCI build](https://circleci.com/gh/DM1st/eatsy/tree/develop.svg?style=shield)](https://circleci.com/gh/DM1st/eatsy/tree/develop)
[![Coverage Status](https://coveralls.io/repos/github/DM1st/eatsy/badge.svg?branch=develop)](https://coveralls.io/github/DM1st/eatsy?branch=develop)
[![Create and publish a Docker image](https://github.com/DM1st/eatsy/actions/workflows/publish.yml/badge.svg)](https://github.com/DM1st/eatsy/actions/workflows/publish.yml)

A live version of the Eatsy App Service swagger ui can be found [here](https://eatsy-recipe-service.herokuapp.com/swagger-ui.html)

## Getting started

The below sections provide the necessary information to run the application locally.

This Java project uses [The Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html), and the
server is run locally as a [Sprint Boot](https://spring.io/projects/spring-boot) application. This can be done from a
bash terminal:

```
./gradlew clean build bootrun
```

Once the server is running navigate to http://localhost:8080/swagger-ui.html

### Please note:

If initiating through an IDE such as intelliJ (rather than command line), you will need to ensure the "Working
Directory"
is correctly set (to the same as the Spring Boot application runner) otherwise you will get initialisation errors when
trying to locate logging config and other properties. The working directory in your IDE run configuration should be set
to:

```
C:\<location to repository root>\eatsyAppService\eatsyAppService-controller
```

### Logging

This project uses log4J2 with the logging config (application.properties and log4j2.properties) located in:

```
eatsyAppService\eatsyAppService-controller\src\main\resources
```

Logs for all Gradle sub-projects write to 'logfile.log' located in the Controller project:

```
\eatsyAppService\eatsyAppService-controller\logs
```

Logging levels for the logfile can be changed in log4j2.properties file.

## API Documentation

A JSON Swagger spec can be found at '/v3/api-docs'. Locally this will be 'http://localhost:8080/v3/api-docs'.

A UI version can be seen using the [Swagger UI page](http://localhost:8080/swagger-ui.html).

## Validation of successful local deployment

This service has unit tests (using [JUnit 5](https://junit.org/junit5/), [Mockito](https://site.mockito.org/)
and [Faker](https://github.com/DiUS/java-faker)) with test coverage displayed on the coverage badge in this ReadMe.
However, the service can be tested manually, see the below example which utilises
the [Swagger UI page](http://localhost:8080/swagger-ui.html):

* Once the service is running navigate to the [Swagger UI page](http://localhost:8080/swagger-ui.html).
* Select the 'Api Controller'
* Select the 'POST' request for adding a new recipe.
* Select "Try it out" and enter a request body of your choice as a test. E.g.

```
{
  "ingredientSet": [
    "Beans", "Toast"
  ],
  "method": {
    "1": "Mircowave Beans",
    "2": "Toast Bread",
    "3": "Combine Beans and Toast"
  },
  "name": "Beans on Toast"
}
```

* Select "Execute".
* Inspect the server response and the response body will display the new recipe object that has been created with your
  chosen name.

## TODO

* Add persistence to service.
* Add Google shopping list integration.
* Add ability to search for recipe.
* Add search capability for best match based on search ingredients.
* Add categories.
* Add tags.
* Add the concept of users.
* Add component test pack.
