#BuginatorClient

Java client library that will capture errors/exceptions and send them to Buginator.

##Features
1. Capture and send errros and exceptions
2. Sync and async http sender
3. Support for servlet params capture
4. Easy integration with Spring/JavaEE
5. Automatic and manual catching of the errors
6. Ssl support (can be turned off - NOT RECOMMENDED)

##Limitations
1. Spring MVC requires to define `@ExceptionHandler` and manually send data
2. Spring @Async exceptions has to be caught by implementing `AsyncUncaughtExceptionHandler`
3. Spring @Schedules must be manually sent from try-catch block
4. JMS listeners exceptions must be manually sent from try-catch block

TODO:
1. Add examples of usage