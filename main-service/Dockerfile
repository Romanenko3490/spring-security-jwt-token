FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY main-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8081"]