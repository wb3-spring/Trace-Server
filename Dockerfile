FROM openjdk:8
MAINTAINER bill@billbensing.com
EXPOSE 3004
ADD /target/wb3-traceserver.jar wb3-traceserver.jar
ENTRYPOINT ["java", "-jar", "wb3-traceserver.jar"]