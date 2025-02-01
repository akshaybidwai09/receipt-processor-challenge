package org.example.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class Receipt {

    @NotBlank(message = "Retailer name cannot be blank")
    @NotNull
    private String retailer;

    @NotBlank(message = "Purchase date is required (YYYY-MM-DD)")
    @NotNull
    private String purchaseDate;

    @NotBlank(message = "Purchase time is required (HH:MM)")
    @NotNull
    private String purchaseTime;

    @NotNull(message = "Items list cannot be null")
    @Valid
    private List<Item> items;

    @NotBlank(message = "Total price is required")
    @NotNull
    private String total;

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
