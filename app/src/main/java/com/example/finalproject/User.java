package com.example.finalproject;

import java.util.ArrayList;

public class User {

    private String password;
    private String userID;
    private String firstName;
    private String lastName;
    private String eMail;
    private String homeUniversity;
    ArrayList<FoodReview> publishedReviews;
    ArrayList<FoodReview> nonPublishedReviews;

    public User() {
    }

    public void setPassword(String pW) {
        password = pW;
    }
    public void setUserID(String uI) {
        userID = uI;
    }
    public void setFirstName(String fN) {
        firstName = fN;
    }
    public void setLastName(String lN) {
        lastName = lN;
    }
    public void setEmail(String eM) {
        eMail = eM;
    }
    public void setHomeUniversity(String hU) {
        homeUniversity = hU;
    }

    public String getPassword() {
        return password;
    }
    public String getUserID() {
        return userID;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return eMail;
    }
    public String getHomeUniversity() {
        return homeUniversity;
    }
    public ArrayList<FoodReview> getPublishedReviews() {
        return publishedReviews;
    }
    public ArrayList<FoodReview> getNonPublishedReviews() {
        return nonPublishedReviews;
    }

    public void PublishReview(FoodReview r) {
        if (r.getFood() != null
        && r.getRestaurant() != null
        && r.getUniversity() != null
        && r.getDate() != null
        && r.getTasteScore() != -1
        && r.getLookScore() != -1
        && r.getTextureScore() != -1
        && r.getAverageScore() != -1
        && r.getReviewText() != null) {
            publishedReviews.add(r);

            //TODO test if review already in restaurant's food's reviews... if no add it to there
        }
    }
}

class AdminUser extends User {
    //TODO add something here
}