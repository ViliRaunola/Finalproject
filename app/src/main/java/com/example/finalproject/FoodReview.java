package com.example.finalproject;

import java.text.DateFormat;

public class FoodReview {

    private String food;
    private String restaurant;
    DateFormat date; //TODO käytetäänkö me tätä?
    private float tasteScore;
    private float lookScore;
    private float textureScore;
    private float averageScore;
    private String reviewText;
    //TODO add picture of food

    public FoodReview(String f, String r, float taScore, float loScore, float teScore, String text) {
        food = f;
        restaurant = r;
        tasteScore = taScore;
        lookScore = loScore;
        textureScore = teScore;
        averageScore = (taScore + loScore + teScore) / 3;
        reviewText = text;
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
}
