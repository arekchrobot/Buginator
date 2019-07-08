FROM openjdk:11

WORKDIR /opt/apps

COPY ./target/Buginator-2.0.0-SNAPSHOT.jar /opt/apps

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "/opt/apps/Buginator-2.0.0-SNAPSHOT.jar"]