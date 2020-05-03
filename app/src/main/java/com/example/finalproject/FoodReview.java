package com.example.finalproject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodReview implements Serializable {

    private String reviewId;
    private String foodId;
    private String foodName;
    private String restaurant;
    private Date date; //TODO käytetäänkö me tätä?
    private float tasteScore;
    private float lookScore;
    private float textureScore;
    private float averageScore;
    private String reviewText;
    private String userId;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private Boolean published;


    public FoodReview(String reviewId,Boolean published, String f, String n, String r, Date d, float taScore, float loScore, float teScore, String text, String userId) {
        foodId = f;
        foodName = n;
        restaurant = r;
        date = d;
        tasteScore = taScore;
        lookScore = loScore;
        textureScore = teScore;
        averageScore = (taScore + loScore + teScore) / 3;
        reviewText = text;
        this.userId = userId;
        this.published = published;
        this.reviewId = reviewId;
    }

    public float getTasteScore() {
        return tasteScore;
    }
    public float getLookScore() {
        return lookScore;
    }
    public float getTextureScore() {
        return textureScore;
    }
    public float getAverageScore() {
        return averageScore;
    }
    public String getReviewText() {
        return reviewText;
    }
    public String getFoodId() {
        return foodId;
    }
    public String getFoodName() {
        return foodName;
    }
    public Date getDate() {
        return date;
    }
    public String getRestaurant(){
     return this.restaurant;
    }
    public String getReviewId(){
        return this.reviewId;
    }
    public String getUserId(){
        return  this.userId;
    }

    public void setReviewText(String text){
        this.reviewText = text;
    }

    public void setTasteScore(float taste){
        this.tasteScore = taste;
    }

    public void setLookScore(float look){
        this.lookScore = look;
    }

    public void setTextureScore(float texture){
        this.textureScore = texture;
    }

    public void setAverageScore(){
        this.averageScore = (this.lookScore + this.textureScore + this.tasteScore) / 3;
    }

    public String getDateString(){
        return simpleDateFormat.format(this.date);
    }

    public String getPublished(){
        return this.published.toString();
    }





    @Override
    public String toString() {
        String temp;
        temp = "Food: " + this.foodName + "\n" + "Date: " + simpleDateFormat.format(this.date) + "\n" + "Restaurant: " + this.restaurant + "\n" + "Average Score: " + String.format("%.1f", this.averageScore);
        return temp;
    }
}
