package com.example.onlineelectronicstore.model;

public class ShoppingCartProducts {

    String productID;
    String cartID;
    String prodcutName;
    String productPrice;
    String productImage;
    String userID;

    public ShoppingCartProducts() {

    }

    public ShoppingCartProducts(String productID, String cartID, String prodcutName, String productPrice, String productImage, String userID) {
        this.productID = productID;
        this.cartID = cartID;
        this.prodcutName = prodcutName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
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
}
