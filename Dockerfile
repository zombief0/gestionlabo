FROM openjdk:11
COPY ./target/labo.war /usr/src/
WORKDIR /usr/src/
EXPOSE 8080
CMD ["java","-jar", "labo.war"]
