FROM openjdk:11
COPY target/eventpublisher-0.0.3.jar eventpublisher-0.0.3.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/eventpublisher-0.0.3.jar"]
