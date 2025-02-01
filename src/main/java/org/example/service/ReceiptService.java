package org.example.service;


import org.example.model.Receipt;
import org.example.receiptUtil.ReceiptUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReceiptService {

    private final Map<String, Receipt> receipts = new ConcurrentHashMap<>();

    private final Map<String, Integer> receiptPoints = new ConcurrentHashMap<>();

    public String processReceipt(Receipt receipt){

        if (!ReceiptUtils.validateReceipt(receipt)) {
            throw new IllegalArgumentException("Invalid receipt data. Please verify input.");
        }
        String id = UUID.randomUUID().toString();
        int points = ReceiptUtils.calculatePoints(receipt);
        receipts.put(id, receipt);
        receiptPoints.put(id, points);
        return id;
    }

    public Integer getPoints(String id){
        return receiptPoints.get(id);
    }
}
