FROM eclipse-temurin:17-jre-alpine
RUN mkdir -p /opt/java/iridium
COPY ./iridium-core-server.jar /opt/java/iridium

WORKDIR /opt/java/iridium

CMD ["java", "-jar", "iridium-core-server.jar"]



