# Buginator

Application that tracks errors and exceptions storing them in database. It has a nice interface to analyze erros that happened in an API, webapp or any other java app.
Additionaly it provides a notification system that can be configured for easier management.

* [Features](#features)
* [Usage](#usage)
* [Prerequisites](#prerequisites)
* [Installation](#installation)
* [Running/Building](#building-and-running)

## Features
1. Manage errors for apps
2. Notifications via email
3. Managing statuses of each exceptions
4. Grouping same exceptions into one error
5. Easy to use interface
6. Weekly statistics about errors frequency

## Usage
Currently working working version is **1.0.0**. The 2.0.0-SNAPSHOT is currently under development and some features may not work (front-end is currently not working).
 

## Prerequisites
To successfully build the application you must have:
1. MySQL/Postgres database called `Buginator` or any db that may be easily supported
2. Maven *3*
3. JDK **11**
4. Apache ArtemisMQ running

## Installation
To install download or clone the project and run `mvn install` on the root project. This should execute and perform all tests.
To create database for local env let hibernate auto generate it (**not suitable for production**).
The auto-ddl will also fill database with test data to play with.

## Building and running
Project cointains 3 maven profiles:
``` sh
develop
integration
mutation
```
The first one is used by default to build local environment and will load springboot-devtools. Second and third are for enabling or disabling execution of integration and mutation tests.
You can manage the build process with following command:
``` sh
mvn clean install -P develop
mvn clean install -P develop,integration,mutation
```

To run a project you need to execute following command:
``` sh
mvn spring-boot:run
```
On all projects in *services* subdirectory.