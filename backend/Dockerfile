FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package \
    && cp target/backend-0.0.1-SNAPSHOT.jar app.jar

FROM eclipse-temurin:25-jre

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd -r spring \
    && useradd -r -g spring spring

COPY --from=build --chown=spring:spring /app/app.jar app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]
