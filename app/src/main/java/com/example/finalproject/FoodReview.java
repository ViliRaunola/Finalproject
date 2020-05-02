package com.example.finalproject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodReview {

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

    public FoodReview(String f, String n, String r, Date d, float taScore, float loScore, float teScore, String text, String userId) {
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


    @Override
    public String toString() {
        String temp;
        temp = "Food: " + this.foodName + "\n" + "Date: " + simpleDateFormat.format(this.date) + "\n" + "Restaurant: " + this.restaurant + "\n" + "Average Score: " + String.format("%.1f", this.averageScore);
        return temp;
    }
}
