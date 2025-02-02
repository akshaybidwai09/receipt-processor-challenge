FROM openjdk:17
WORKDIR /app
COPY target/receipt-processor.jar app.jar
EXPOSE 9000
CMD ["java", "-jar", "app.jar"]