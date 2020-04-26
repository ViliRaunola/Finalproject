package com.example.finalproject;

import java.util.ArrayList;

public class Restaurant {

    private String name;
    public ArrayList<String> restaurantMenusXML = new ArrayList<String>();
    public ArrayList<FoodItem> resDailyMenu = new ArrayList<FoodItem>();

    public Restaurant(String name){
        this.name = name;
    }

    public void addToRestaurantMenusXML(String menu) {
        restaurantMenusXML.add(menu);
    }

    public void addFoodItemToDailyMenu(FoodItem o) {
        resDailyMenu.add(o);
    }

    @Override
    public String toString() {
        String temp;
        temp = this.name;
        return temp;
    }


}
