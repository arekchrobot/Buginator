FROM openjdk:11

WORKDIR /opt/apps

COPY ./target/BuginatorAuth-2.0.0-SNAPSHOT.jar /opt/apps

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "/opt/apps/BuginatorAuth-2.0.0-SNAPSHOT.jar"]