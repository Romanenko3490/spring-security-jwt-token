FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY auth-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8082"]