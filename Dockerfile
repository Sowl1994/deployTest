FROM eclipse-temurin:17-alpine

RUN mkdir /app

WORKDIR /app

COPY /target/userapi-0.0.1-SNAPSHOT.jar /app/userapi.jar

EXPOSE 8080

CMD java -jar userapi.jar