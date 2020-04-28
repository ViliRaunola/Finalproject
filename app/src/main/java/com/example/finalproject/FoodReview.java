package com.example.finalproject;

public class FoodReview {


    private float tasteScore;
    private float lookScore;
    private float textureScore;
    private float averageScore;
    private String reviewText;
    //TODO add picture of food

    public FoodReview(float taScore, float loScore, float teScore, String text) {
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
