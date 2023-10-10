FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y wget tar

RUN wget -q https://download.java.net/java/GA/jdk20/bdc68b4b9cbc4ebcb30745c85038d91d/36/GPL/openjdk-20_linux-x64_bin.tar.gz -O /tmp/openjdk20.tar.gz && \
    tar xf /tmp/openjdk20.tar.gz -C /usr/local/

COPY . .

ENV PATH="/usr/local/openjdk20/bin:${PATH}"
ENV JAVA_HOME=/usr/local/openjdk20

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:20-jdk-slim

EXPOSE 8080

COPY --from=build /target/camel.notes-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar"]
