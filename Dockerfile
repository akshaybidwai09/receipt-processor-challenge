FROM maven:3.8.7-openjdk-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/receipt-processor-1.0-SNAPSHOT.jar app.jar

EXPOSE 9000

# Run the application
CMD ["java", "-jar", "app.jar"]
