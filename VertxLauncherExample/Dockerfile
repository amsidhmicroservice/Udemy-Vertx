FROM openjdk:8-jdk-alpine

ENV FAT_JAR VertxLauncherExample-1.0.0-SNAPSHOT-fat.jar
ENV APP_HOME /usr/app

EXPOSE 8888

COPY /target/$FAT_JAR $APP_HOME/

WORKDIR $APP_HOME

ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $FAT_JAR"]




