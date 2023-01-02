FROM docker.io/library/eclipse-temurin:19
ARG JAR=build/libs/helloworld-spring-*.jar

ADD $JAR /helloworld.jar
USER 1000:1000
ENTRYPOINT ["java", "-jar", "/helloworld.jar"]