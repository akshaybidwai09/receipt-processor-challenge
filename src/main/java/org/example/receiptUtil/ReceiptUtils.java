package org.example.receiptUtil;

import org.example.model.Item;
import org.example.model.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class ReceiptUtils {

    // I have assumed the program is not generated using LLM but if it does, we must have a service to detect that and then update this field;
    private static boolean ENABLE_LLM_ = false;


    public static int calculatePoints(Receipt receipt){

        int points = 0;

        points += receipt.getRetailer().replaceAll("\\W", "").length();


        if(isRoundDollarAmount(receipt.getTotal())) {
            points += 50;
        }

        if(isMultipleofQuarter(receipt.getTotal())){
            points += 25;
        }

        points += (receipt.getItems().size() / 2) * 5;


        for (Item item : receipt.getItems()) {
            int descriptionLength = item.getShortDescription().trim().length();
            if (descriptionLength % 3 == 0) {
                points += calculateItemPoints(item.getPrice());
            }
        }

        if (ENABLE_LLM_ && Double.parseDouble(receipt.getTotal()) > 10.00) {
            points += 5;
        }

        if(isDayOdd(receipt.getPurchaseDate())){
            points += 6;
        }

        if(isBetweenTwoAndFourPM(receipt.getPurchaseTime())){
            points += 10;
        }

        return points;

    }

    private static int calculateItemPoints(String price) {
        double value = Double.parseDouble(price) * 0.2;

        return (int) Math.ceil(value);
    }

    private static boolean isRoundDollarAmount(String total) {
        BigDecimal amount = new BigDecimal(total);
        return amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0;
    }

    private static boolean isMultipleofQuarter(String total) {
        return Double.parseDouble(total) % 0.25 == 0;
    }

    private static boolean isDayOdd(String dateStr) {
        return LocalDate.parse(dateStr).getDayOfMonth() % 2 != 0;
    }

    private static boolean isBetweenTwoAndFourPM(String timeStr) {
        LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
        return !time.isBefore(LocalTime.of(14, 0)) && time.isBefore(LocalTime.of(16, 0));
    }

    public static String validateReceipt(Receipt receipt) {

        /**
         * Null validation is already managed on Object level via annotation below null validation are
         * for validating sample tests written in ReceiptServiceTest.java file which by passes controller
         * */
        if (receipt == null) {
            return "Receipt cannot be null.";
        }
        if (receipt.getRetailer() == null || receipt.getRetailer().trim().isEmpty()) {
            return "Retailer name cannot be blank.";
        }
        if (receipt.getPurchaseDate() == null || receipt.getPurchaseDate().trim().isEmpty()) {
            return "Purchase date is required (YYYY-MM-DD).";
        }

        try {
            LocalDate.parse(receipt.getPurchaseDate()); // Throws exception if invalid
        } catch (DateTimeParseException e) {
            return "Invalid purchase date format. Expected format: YYYY-MM-DD.";
        }

        if (receipt.getPurchaseTime() == null || receipt.getPurchaseTime().trim().isEmpty()) {
            return "Purchase time is required (HH:MM).";
        }

        try {
            LocalTime.parse(receipt.getPurchaseTime()); // Validates proper HH:MM format (24-hour)
        } catch (DateTimeParseException e) {
            return "Invalid purchase time format. Expected format: HH:MM (24-hour format).";
        }

        if (receipt.getItems() == null || receipt.getItems().isEmpty()) {
            return "Items list cannot be empty.";
        }
        if (receipt.getTotal() == null || receipt.getTotal().trim().isEmpty()) {
            return "Total price is required.";
        }
        // Additional validation for numeric fields
        try {
            BigDecimal totalAmount = new BigDecimal(receipt.getTotal());
            if (totalAmount.scale() > 2) {
                return "Total price must have at most two decimal places (e.g., 6.49).";
            }
            if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
                return "Total price cannot be negative.";
            }
        } catch (NumberFormatException e) {
            return "Invalid total price format. Must be a valid number (e.g., 6.49).";
        }

        for (Item item : receipt.getItems()) {
            if (item.getShortDescription() == null || item.getShortDescription().trim().isEmpty()) {
                return "Each item must have a short description.";
            }
            if (item.getPrice() == null || item.getPrice().trim().isEmpty()) {
                return "Each item must have a price.";
            }
            try {
                BigDecimal price = new BigDecimal(item.getPrice());
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    return "Price can not be negative";
                }
            } catch (NumberFormatException e) {
                return "Invalid price format for an item.";
            }
        }

        return null;
    }


    private static boolean ENABLE_LLM_BONUS = false;
}
