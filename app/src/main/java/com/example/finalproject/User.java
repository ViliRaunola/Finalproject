package com.example.finalproject;

import java.util.ArrayList;
import java.util.Objects;


public class User {

    private boolean isAdminUser = false;
    private String password;
    private int userId;
    private String firstName;
    private String lastName;
    private String eMail;
    private int homeUniversity;
    ArrayList<FoodReview> publishedReviews;
    ArrayList<FoodReview> nonPublishedReviews;
    private static User user = new User();
    private User() {
    }
    public static User getInstance(){
        return user;
    }
    public void setPassword(String pW) {
        password = pW;
    }
    public void setUserID(int uI) {
        userId = uI;
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
    public void setHomeUniversity(int hU) {
        homeUniversity = hU;
    }
    //Moi vili
    public boolean getIsAdminUser() {
        return isAdminUser;
    }
    public String getPassword() {
        return password;
    }
    public int getUserID() {
        return userId;
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
    public int getHomeUniversity() {
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