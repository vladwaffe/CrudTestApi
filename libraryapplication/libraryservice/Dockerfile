FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build
COPY build/libs/*.jar /app/LibraryService.jar
ENTRYPOINT ["java","-jar","/app/LibraryService.jar"]