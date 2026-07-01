# Perform the extraction in a separate builder container
FROM eclipse-temurin:8-jdk AS builder
WORKDIR /builder
# This points to the built jar file in the target folder
ARG JAR_FILE=build/libs/*.jar
# Copy the jar file to the working directory and rename it to application.jar
COPY ${JAR_FILE} app.jar
# Extract the jar file using an efficient layout (Spring Boot 2.x layertools)
RUN java -Djarmode=layertools -jar app.jar extract --destination extracted

FROM eclipse-temurin:8-jre

ENV APP_DIR=/data/app
ENV SPRING_PROFILE=prod
ENV JAVA_OPTS=-Xmx512M
ENV LOG_FILE=/data/logs/app.log
ENV MAIN_CLASS=net.sunshow.badge.gateway.BadgeGatewayApplicationKt

WORKDIR $APP_DIR

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

ENTRYPOINT ["entrypoint.sh"]
