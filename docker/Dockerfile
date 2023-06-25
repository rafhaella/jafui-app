FROM openjdk:19-jdk-alpine3.16
RUN apk add --no-cache --upgrade bash
ENV APP_AWS_ACCESSKEY=
ENV APP_AWS_SECRETKEY=
WORKDIR /usr/src
ARG JAR_FILE 
COPY ${JAR_FILE} /usr/src/app.jar
COPY app.sh app.sh
COPY application.properties application.properties
RUN sed -i 's/\r$//' app.sh
RUN chmod 755 app.sh
EXPOSE 8080
ENTRYPOINT ["/bin/bash","/usr/src/app.sh"]