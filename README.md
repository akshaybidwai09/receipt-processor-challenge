# Receipt Processor Challenge

#### This project is implemented in Java Spring Boot. You only need to have Docker installed on your machine. A Docker script has been created that downloads the base JDK and Maven images, allowing you to run the project without any manual installation.

## Overview
This project implements a **Receipt Processing API** that assigns points to receipts based on various rules.

### 1. Rules for Points Calculation
The following rules collectively define how many points should be awarded to a receipt:

- **1 point** for every alphanumeric character in the **retailer name**.
- **50 points** if the **total** is a **round dollar amount** with no cents (e.g., `10.00`).
- **25 points** if the **total** is a **multiple of 0.25** (e.g., `10.25`, `20.50`).
- **5 points** for every **two items** on the receipt.
- If the **trimmed length** of an **item description** is a multiple of 3:
  - Multiply the item price by `0.2` and round up to the nearest integer.
  - The result is the number of points earned.
- **6 points** if the **purchase date** (day) is **odd**.
- **10 points** if the **purchase time** is between **2:00 PM and 4:00 PM**.
- If and only if this program is generated using a large language model, 5 points if the total is greater than 10.00.

---

## 2. APIs Used
The following endpoints are exposed:

| **Endpoint**               | **Method** | **Description** |
|----------------------------|-----------|----------------|
| `/receipts/process`        | **POST**  | Submits a receipt for processing and returns a generated `receipt ID`. |
| `/receipts/{id}/points`    | **GET**   | Retrieves the total points awarded for a given `receipt ID`. |

---

## 3. Prerequisites

- **Docker**
- **Java 17** **(Dont need to have in your machine, build command will use java base image)**
- **Maven** **(Dont need to have in your machine, build command will use maven base image)**
- **I have created dockerfile script that installs all the necessary libraries without manual work.**
---

## 4. How to Run This Project

### **Option 1: Pull the Image from Docker Hub**
If you want to run the project using the pre-built Docker image, follow these steps:

```sh
docker login  # Enter your Docker Hub credentials when prompted
docker pull akshayvijaybidwai/receipt-processorv1
docker run -p 9000:9000 akshayvijaybidwai/receipt-processorv1
```

---

### **Option 2: Clone the Repository and Build the Docker Image**
If you want to build the project locally from source, follow these steps:

1. **Clone the GitHub Repository:**
   ```sh
   git clone https://github.com/akshaybidwai09/receipt-processor-challenge.git
   cd receipt-processor-challenge
   ```

2. **Build the Docker Image:**
   ```sh
   docker build -t receipt-processor .
   ```

3. **Run the Docker Container:**
   ```sh
   docker run -p 9000:9000 receipt-processor
   ```

---


### 5. **Project Structure**
```
receipt-processor-challenge
│── src
│   ├── main
│   │   ├── java/com/example/receiptprocessor
│   │   │   ├── ReceiptProcessorApplication.java  # Main application entry
│   │   │   ├── controller
│   │   │   │   ├── ReceiptController.java       # Handles API requests
│   │   │   ├── model
│   │   │   │   ├── Receipt.java                 # Defines Receipt Model
│   │   │   │   ├── Item.java                    # Defines Item Model
│   │   │   │   ├── ResponseDTO.java             # Response Data Model
│   │   │   ├── service
│   │   │   │   ├── ReceiptService.java          # Core logic for processing receipts
│   │   │   ├── receiptUtil
│   │   │   │   ├── ReceiptUtils.java            # Utility for calculating points
│   ├── test
│   │   ├── java/com/example/receiptprocessor
│   │   │   ├── ReceiptServiceTest.java          # Unit tests
│── pom.xml
│── Dockerfile
│── README.md
│── src/main/resources/application.properties
```

---

### 6.. **Testing the API**

#### **Submit a Receipt (POST /receipts/process)**
**Once any of the above two options from (How to Run This Project) section are done, then on the different terminal under the same path please execute the below curl commands.**

Modify the request parameters as needed and run the following command:
```sh
curl -X POST "http://localhost:9000/receipts/process" \
     -H "Content-Type: application/json" \
     -d '{
          "retailer": "Target",
          "purchaseDate": "2022-01-01",
          "purchaseTime": "13:01",
          "items": [
            { "shortDescription": "Mountain Dew 12PK", "price": "6.49" },
            { "shortDescription": "Emils Cheese Pizza", "price": "12.25" },
            { "shortDescription": "Knorr Creamy Chicken", "price": "1.26" },
            { "shortDescription": "Doritos Nacho Cheese", "price": "3.35" },
            { "shortDescription": "Klarbrunn 12-PK 12 FL OZ", "price": "12.00" }
          ],
          "total": "35.35"
        }'
```

#### 7. **Expected Response:**
```json
{
  "id": "your-generated-receipt-id"
}
```

---

### 8. **Retrieve Points (GET /receipts/{id}/points)**
```sh
curl -X GET "http://localhost:9000/receipts/{id}/points"
```

#### **Expected Response:**
```json
{
  "points": final_points
}
```

---

---

### 9. **Error Handling**
To ensure data consistency and proper validation, the following constraints must be met:
- **Date format:** `YYYY-MM-DD`
- **Time format:** `HH:MM` (24-hour format)
- **Total and item prices must be valid numbers** (e.g., `10.00`, `5.25`) and the number cannot be negative!
- **Retailer name must be a valid string**
- **All required fields must be present in the request**

If the input is invalid, an appropriate error message will be returned.



