package com.example.finalproject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodReview implements Serializable  {

    //string for translation
    private String foodString;
    private String dateString;
    private String restaurantString;
    private String averageScoreString;
    private String voteScoreString;

    private String reviewId;
    private String foodId;
    private String foodName;
    private String restaurant;
    private Date date;
    private Float tasteScore;
    private Float lookScore;
    private Float textureScore;
    private Float averageScore;
    private String reviewText;
    private String userId;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private Boolean published;
    private Integer voteScore;

    public FoodReview(){

    }

    public FoodReview(String reviewId,Boolean published, String f, String n, String r, Date d, float taScore, float loScore, float teScore, String text, String userId, Integer voteScore) {
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
        this.voteScore = voteScore;
    }

    //get methods
    public float getTasteScore() {
        return tasteScore;
    }
    public float getLookScore() {
        return lookScore;
    }
    public float getTextureScore() {
        return textureScore;
    }
    public Float getAverageScore() {
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
    public int getVoteScore() {
        return voteScore;
    }
    public String getDateString(){
        return simpleDateFormat.format(this.date);
    }
    public String getPublished(){
        return this.published.toString();
    }
    public Boolean getPublishedBoolean(){
        return this.published;
    }

    //set methods
    public void setPublished(Boolean b) {
        this.published = b;
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
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public void setFoodString(String foodString) {
        this.foodString = foodString;
    }
    public void setAverageScoreString(String averageScoreString) {
        this.averageScoreString = averageScoreString;
    }
    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
    public void setRestaurantString(String restaurantString) {
        this.restaurantString = restaurantString;
    }
    public void setVoteScoreString(String voteScoreString) {
        this.voteScoreString = voteScoreString;
    }
    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
    public void setDate(String date) {
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            this.date = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void setReviewId(String id){
        this.reviewId = id;
    }
    public void setVoteScore(Integer voteScore) {
        this.voteScore = voteScore;
    }

    //method for changing vote score
    public void changeVoteScore(Integer amount) {
        this.voteScore += amount;
    }


    /*
    This "toString" method is for array adapter to show selected string.
    In this case food review.
    */
    @Override
    public String toString() {
        String temp;

        temp = foodString + ": " + this.foodName + "\n"
                + dateString + ": " + simpleDateFormat.format(this.date) + "\n"
                + restaurantString +": " + this.restaurant + "\n"
                + averageScoreString + ": " + String.format("%.1f", this.averageScore) + "\n"
                + voteScoreString+": " + this.voteScore;
        return temp;
    }
}
