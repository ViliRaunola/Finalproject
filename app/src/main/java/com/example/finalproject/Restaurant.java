package com.example.finalproject;

import java.util.ArrayList;

public class Restaurant {

    private String name;
    public ArrayList<FoodItem> restaurantMenus = new ArrayList<FoodItem>();

    public Restaurant(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        String temp;
        temp = this.name;
        return temp;
    }


}
