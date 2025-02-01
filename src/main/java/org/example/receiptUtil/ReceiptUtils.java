package org.example.receiptUtil;

import org.example.model.Item;
import org.example.model.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



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

    public static boolean validateReceipt(Receipt receipt) {
        if (receipt == null ||
                receipt.getRetailer() == null || receipt.getRetailer().trim().isEmpty() ||
                receipt.getPurchaseDate() == null || receipt.getPurchaseDate().trim().isEmpty() ||
                receipt.getPurchaseTime() == null || receipt.getPurchaseTime().trim().isEmpty() ||
                receipt.getTotal() == null || receipt.getTotal().trim().isEmpty() ||
                receipt.getItems() == null || receipt.getItems().isEmpty()) {
            return false;
        }

        // Validate Total
        try {
            BigDecimal totalAmount = new BigDecimal(receipt.getTotal());
            if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // Validate Items
        for (Item item : receipt.getItems()) {
            if (item.getShortDescription() == null || item.getShortDescription().trim().isEmpty() ||
                    item.getPrice() == null || item.getPrice().trim().isEmpty()) {
                return false;
            }
            try {
                BigDecimal price = new BigDecimal(item.getPrice());
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // Validate Date & Time Formats
        try {
            LocalDate.parse(receipt.getPurchaseDate());
            LocalTime.parse(receipt.getPurchaseTime(), DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private static boolean ENABLE_LLM_BONUS = false;
}
