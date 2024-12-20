FROM gradle:8.11.1-jdk23-alpine AS builder
LABEL authors="Jeremy CLEMENT"

ENV SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
ENV SPRING_DATASOURCE_DRIVER-CLASS-NAME=org.h2.Driver
ENV SPRING_DATASOURCE_USERNAME=sa
ENV SPRING_DATASOURCE_PASSWORD=sa

USER root
WORKDIR /app

COPY . .

RUN gradle build --no-daemon

FROM eclipse-temurin:23.0.1_11-jre-alpine

ARG JAR_FILE
ENV JAR_FILE=${JAR_FILE}
ENV JAVA_OPTS="-Dnetworkaddress.cache.ttl=5 -Dnetworkaddress.cache.negative.ttl=5"
ENV APP_BASE_DIR=/app

USER root
WORKDIR /app

COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

COPY ./docker/app/. .
COPY --from=builder /app/build/libs/${JAR_FILE:-*.jar} /app/app.jar

EXPOSE 8080
HEALTHCHECK --start-period=15s --interval=30s --timeout=3s --retries=2 CMD wget http://localhost:8080/actuator/health -q -O - 2>&1 | grep UP

ENTRYPOINT ["/docker-entrypoint.sh"]
