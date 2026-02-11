FROM maven:3.9.6-amazoncorretto-21-debian AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM amazoncorretto:21
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 5050

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]

LABEL maintainer="caue.gprieto@gmail.com"
LABEL description="Encurtador de URL com QR Code"