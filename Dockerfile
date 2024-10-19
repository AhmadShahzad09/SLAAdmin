FROM adoptopenjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ms-admin.jar
EXPOSE ${SLA_ADMIN_PORT}
ENTRYPOINT [ "java", "-jar", "/ms-admin.jar" ]