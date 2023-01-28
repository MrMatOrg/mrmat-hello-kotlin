FROM docker.io/library/eclipse-temurin:19
ADD helloworld.jar /
USER 1000:1000
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "/helloworld.jar"]
