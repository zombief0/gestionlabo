FROM openjdk:11
COPY ./target/labo.war /usr/src/myapp
WORKDIR /usr/src/myapp
EXPOSE 8080
RUN java -jar labo.war
