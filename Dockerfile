FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Run Maven build to generate the JAR file
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/receipt-processor-1.0-SNAPSHOT.jar app.jar

EXPOSE 9000

CMD ["java", "-jar", "app.jar"]
