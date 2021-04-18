package com.example.onlineelectronicstore.model;

public class productRating {

    String productID;
    String userID;
    float rateValue;
    String review;
    String ratingID;

    public productRating() {

    }

    public productRating(String productID, String userID, float rateValue, String review, String ratingID) {
        this.productID = productID;
        this.userID = userID;
        this.rateValue = rateValue;
        this.review = review;
        this.ratingID = ratingID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public float getRateValue() {
        return rateValue;
    }

    public void setRateValue(float rateValue) {
        this.rateValue = rateValue;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }
}
