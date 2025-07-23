package com.example.store.models;

public class Order {
    private String customerName;
    private String productCode;
    private String timestamp;

    public Order() {
        // Απαιτείται κενός constructor για τη Firebase
    }

    public Order(String customerName, String productCode, String timestamp) {
        this.customerName = customerName;
        this.productCode = productCode;
        this.timestamp = timestamp;
    }

    // Getters και Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
