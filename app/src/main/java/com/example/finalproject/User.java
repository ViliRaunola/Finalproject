package com.example.finalproject;

public class User {

    private String password;
    private String userID;
    private String firstName;
    private String lastName;
    private String eMail;
    private String homeUniversity;
    //TODO add picture here

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
}

class AdminUser extends User {
    //TODO add something here
}