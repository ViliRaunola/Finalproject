package com.example.finalproject;

import java.util.ArrayList;
import java.util.Objects;


public class User {

    private boolean isAdminUser;
    private String password;
    private int userId;
    private String firstName;
    private String lastName;
    private String eMail;
    private int homeUniversityPos;
    private String homeUniversity;
    private ArrayList<String> upVotedList = new ArrayList<String>();
    private ArrayList<String> downVotedList = new ArrayList<String>();;

    //Makes this singleton
    private static User user = new User();
    private User() {
    }
    public static User getInstance(){
        return user;
    }


    //Set methods
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
    public void setHomeUniversity(String hU) {
        homeUniversity = hU;
    }
    public void setHomeUniversityPos(int hU) {
        homeUniversityPos = hU;
    }
    public void setAdminUser(boolean isAdminUser){
        this.isAdminUser = isAdminUser;
    }
    public void setUpVotedList(ArrayList<String> upVotedList){
        this.upVotedList = upVotedList;
    }
    public void setDownVotedList(ArrayList<String> upVotedList){
        this.upVotedList = upVotedList;
    }

    //Get methods
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
    public int getHomeUniversityPos() {
        return homeUniversityPos;
    }
    public String getHomeUniversity() {
        return homeUniversity;
    }
    public ArrayList<String> getUpVotedList() {
        return upVotedList;
    }
    public ArrayList<String> getDownVotedList() {
        return downVotedList;
    }
}