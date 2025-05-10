FROM eclipse-temurin:21-alpine
RUN mkdir /springconfig
COPY ./target/labo.jar /usr/src/
WORKDIR /usr/src/
EXPOSE 8080
CMD ["java","-jar", "labo.jar", "--spring.config.import=/springconfig/"]
