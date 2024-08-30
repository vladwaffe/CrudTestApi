FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build 

COPY build/libs/LibraryService-plain.jar LibraryService.jar
ENTRYPOINT ["java","-jar","/LibraryService.jar"]