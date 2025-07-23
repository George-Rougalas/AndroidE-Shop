package com.example.store.models;

import java.io.Serializable;
public class Products  implements Serializable{

    String productCode;
    String productCategory;
    String productName;
    String productQty;
    Double productPrice;
    String imageUrl;
    String productDescription;
    Double storeLat;
    Double storeLong;


    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String description) {
        this.productDescription = description;
    }


    int numberinCart;

    public int getNumberinCart() {
        return numberinCart;
    }

    public void setNumberinCart(int numberinCart) {
        this.numberinCart = numberinCart;
    }

    //    public Products() {
//        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
//    }
    public Products(String imageUrl, int numberinCart, String productCategory, String productCode, String productName, Double productPrice, String productQty, String productDescription, Double storeLat, Double storeLong) {
        this.productCategory = productCategory;
        this.productCode = productCode;
        this.productName = productName;
        this.productQty = productQty;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.imageUrl = imageUrl;
        this.numberinCart = numberinCart;
        this.storeLat = storeLat;
        this.storeLong = storeLong;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public Double getProductPrice() {
        return productPrice;
    }
    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public Double getStoreLat() {
        return storeLat;
    }
    public Double getStoreLong() {
        return storeLong;
    }
}
