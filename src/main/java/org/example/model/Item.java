package org.example.model;

import jakarta.validation.constraints.NotBlank;

public class Item {

    public Item() {
    }

    public Item(String shortDescription, String price) {
        this.shortDescription = shortDescription;
        this.price = price;
    }

    @NotBlank(message = "Short description cannot be blank")
    private String shortDescription;

    @NotBlank(message = "Price cannot be blank")
    private String price;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
