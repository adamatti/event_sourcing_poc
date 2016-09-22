FROM java:8

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY . /usr/src/app

RUN ./gradlew idea

EXPOSE 8080
CMD ./gradlew run
