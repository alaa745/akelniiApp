package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "GoogleCart", foreignKeys = {@ForeignKey(entity = Product.class,
        parentColumns = "productId",
        childColumns = "pId",
        onDelete = ForeignKey.CASCADE) ,

        @ForeignKey(entity = GoogleUser.class,
                parentColumns = "email",
                childColumns = "userEmail",
                onDelete = ForeignKey.CASCADE),})
public class GoogleUserCart{
    int productPrice , productQuantity;

    String productName , productImage;

    String userEmail;
    @NonNull
    @PrimaryKey
    String pId;

    @Ignore
    GoogleUserCart(){
    }
    GoogleUserCart(@NonNull String pId , String productImage , String productName , int productPrice , int productQuantity , String userEmail){
        this.pId = pId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productImage = productImage;
        this.userEmail = userEmail;
    }

    @NonNull
    public String getpId() {
        return pId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
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
