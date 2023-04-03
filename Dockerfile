FROM openjdk:11
COPY target/eventsubscriber-0.0.2.jar eventsubscriber-0.0.2.jar
EXPOSE 8083
EXPOSE 5672
ENTRYPOINT ["java","-jar","/eventsubscriber-0.0.2.jar"]