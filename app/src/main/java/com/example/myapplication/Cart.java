package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Product.class,
        parentColumns = "productId",
        childColumns = "pId",
        onDelete = ForeignKey.CASCADE) ,

        @ForeignKey(entity = User.class,
        parentColumns = "mobile",
        childColumns = "userMobile",
        onDelete = ForeignKey.CASCADE),})
public class Cart {
    int productPrice , productQuantity;

    String productName , productImage;

    String userMobile;
    @NonNull
    @PrimaryKey
    String pId;

    @Ignore
    Cart(){
    }
    Cart(@NonNull String pId , String productImage , String productName , int productPrice , int productQuantity , String userMobile){
        this.userMobile = userMobile;
        this.pId = pId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productImage = productImage;

    }

    @NonNull
    public String getpId() {
        return pId;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    //    public String getProductId() {
//        return productId;
//    }
//
//    public void setProductId(String productId) {
//        this.productId = productId;
//    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

}
