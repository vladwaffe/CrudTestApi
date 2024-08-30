FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY . .

RUN chmod +x gradlew
RUN ./gradlew build
COPY build/libs/tetsCrudApp-plain.jar tetsCrudApp.jar
ENTRYPOINT ["java","-jar","/tetsCrudApp.jar"]