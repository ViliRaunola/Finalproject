package com.example.finalproject;

import java.util.ArrayList;

public class Restaurant {

    private String name;
    public ArrayList<String> restaurantMenus = new ArrayList<String>();


    public Restaurant(String name){
        this.name = name;
    }

    public void addToRestaurantMenus(String menu) {
        restaurantMenus.add(menu);
    }

    @Override
    public String toString() {
        String temp;
        temp = this.name;
        return temp;
    }


}
