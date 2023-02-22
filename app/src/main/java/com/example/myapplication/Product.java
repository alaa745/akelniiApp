package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Product {
    private String link;
    private String productName;
    @NonNull
    @PrimaryKey
    private String productId;
    private int productPrice;
    private int productQuantity;

    @Ignore
    public Product(){

    }
    public Product(String link, String productName , String productId , int productPrice , int productQuantity) {
        if (productName.trim().equals(""))
            this.productName = "No Name";

        this.link = link;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

//    public String getUrl() {
//        return link;
//    }
//
//    public void setUrl(String link) {
//        this.link = link;
//    }

//    public String getName() {
//        return productName;
//    }
//
//    public void setName(String productName) {
//        this.productName = productName;
//    }
}
