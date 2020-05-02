package com.example.finalproject;

import java.text.DateFormat;
import java.util.Date;

public class FoodReview {

    private String foodId;
    private Date date; //TODO käytetäänkö me tätä?
    private float tasteScore;
    private float lookScore;
    private float textureScore;
    private float averageScore;
    private String reviewText;
    private String userId;

    public FoodReview(String f, Date d, float taScore, float loScore, float teScore, String text, String userId) {
        foodId = f;
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
    public String getFood() {
        return foodId;
    }
    public Date getDate() {
        return date;
    }
}
