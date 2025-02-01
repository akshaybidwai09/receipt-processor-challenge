package org.example.receiptprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example")
public class ReceiptProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReceiptProcessorApplication.class, args);
    }
}


