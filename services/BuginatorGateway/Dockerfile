FROM openjdk:11

WORKDIR /opt/apps

COPY ./target/BuginatorGateway-2.0.0-SNAPSHOT.jar /opt/apps

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "-Dreactor.netty.http.server.accessLogEnabled=true", "/opt/apps/BuginatorGateway-2.0.0-SNAPSHOT.jar"]