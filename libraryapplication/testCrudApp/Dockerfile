FROM openjdk:17-jdk-alpine


RUN apk add --no-cache wget

WORKDIR /app


RUN mkdir -p /app/libs

RUN wget -O /app/libs/postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.2.18.jar

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build

COPY build/libs/*.jar /app/testCrudApp.jar

ENTRYPOINT ["java","-jar","/app/testCrudApp.jar"]

