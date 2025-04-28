FROM eclipse-temurin:21-alpine
COPY ./target/labo.jar /usr/src/
WORKDIR /usr/src/
EXPOSE 8080
CMD ["java","-jar", "labo.jar"]
