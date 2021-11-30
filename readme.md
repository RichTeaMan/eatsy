# Eatsy App Service

A REST API and java service to create, edit and store your favourite recipes.

## Getting started

This Java project uses [The Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).

The below sections provide the necessary information to run the application locally.

The server is run locally as a Sprint Boot application. This can be done from a bash terminal:

```
./gradlew build bootrun
```

Once the server is running navigate to http://localhost:8080/swager-ui/

## API Documentation

A JSON Swagger spec can be found at '/v2/api-docs'. Locally this will be 'http://localhost:8080/v2/api-docs'.

A UI version can be seen using the [Swagger UI page](http://localhost:8080/swagger-ui/).

## Validation of successful local deployment

* Once the service is running navigate to the [Swagger UI page](http://localhost:8080/swagger-ui/).
* Select the 'Api Controller'
* Select the 'GET' request for adding a new recipe.
* Select "Try it out" and enter a recipe name of your choice in the parameter field for a recipe name.
* Select "Execute".
* Inspect the server response and the response body will display the new recipe object that has been created with your
  chosen name. 


