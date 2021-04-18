package com.example.onlineelectronicstore.model;

public class Order {

    String productID;
    String orderID;
    String prodcutName;
    String productPrice;
    String productImage;
    String userID;
    String orderEmail;

    public Order() {

    }

    public Order(String productID, String orderID, String prodcutName, String productPrice, String productImage, String userID, String orderEmail) {
        this.productID = productID;
        this.orderID = orderID;
        this.prodcutName = prodcutName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.userID = userID;
        this.orderEmail = orderEmail;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProdcutName() {
        return prodcutName;
    }

    public void setProdcutName(String prodcutName) {
        this.prodcutName = prodcutName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderEmail() {
        return orderEmail;
    }

    public void setOrderEmail(String orderEmail) {
        this.orderEmail = orderEmail;
    }
}
