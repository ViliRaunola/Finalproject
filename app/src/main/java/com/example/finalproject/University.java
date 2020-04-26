package com.example.finalproject;

import java.util.ArrayList;

public class University {

    private String name;
    private String id;
    ArrayList<String> restaurantsXML = new ArrayList<>();

    University(String name, String id){
        this.name = name;
        this.id = id;
    }
    void addToRestaurants(String restaurant){
        restaurantsXML.add(restaurant);
    }

    @Override
    public String toString() {
        String temp;
        temp = this.name;
        return temp;
    }


}
