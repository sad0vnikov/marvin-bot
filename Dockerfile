FROM openjdk:8
ADD ./core/target/marvin-core-0.2.0/ /app
VOLUME /app
WORKDIR /app
EXPOSE 8080
ENTRYPOINT exec java -jar -Dlog4j.configurationFile=log4j2.xml marvin-core-0.2.0.jar