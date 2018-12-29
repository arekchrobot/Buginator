# Buginator

Application that tracks errors and exceptions storing them in database. It has a nice interface to analyze erros that happened in an API, webapp or any other java app.
Additionaly it provides a notification system that can be configured for easier management.

## Features
1. Manage errors for apps
2. Notifications via email
3. Managing statuses of each exceptions
4. Grouping same exceptions into one error

## Prerequisites
To successfully run the application you must have:
1. MySQL/Postgres database called `Buginator`
2. Maven
3. Java version **11**
4. Apache ArtemisMQ running

## Running/Building
Project cointains 3 maven profiles:
```sh
develop
integration
mutation
```
The first one is used by default to build local enviornment and will load springboot-devtools. Second and third are for enabling or disabling execution of intergation and mutation tests.
You can manage the build process with following command:
```sh
mvn clean install -P develop
mvn clean install -P develop,integration,mutation
```

To run a project you need to execute following command:
```sh
mvn spring-boot:run
```
For those services:
1. BuginatorAuth
2. Buginator