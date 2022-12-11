FROM openjdk:11
COPY target/jlifx-*.jar app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/app.jar"]
