package com.example.finalproject;

import java.util.ArrayList;

public class University {

    private String name;
    private String id;
    ArrayList<String> restaurants = new ArrayList<>();

    University(String name, String id){
        this.name = name;
        this.id = id;
    }
    void addToRestaurants(String restaurant){
        restaurants.add(restaurant);
    }


}
