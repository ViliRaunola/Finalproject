package com.example.finalproject;

import java.io.Serializable;
import java.util.ArrayList;

//Implements Serializable so instance of this class can be sent between fragments through bundles.
public class Restaurant implements Serializable {
    private String name;
    public ArrayList<String> restaurantMenusXML = new ArrayList<String>();
    public ArrayList<FoodItem> resDailyMenu = new ArrayList<FoodItem>();

    //Constructor for this class
    public Restaurant(String name){
        this.name = name;
    }

    public void addToRestaurantMenusXML(String menu) {
        restaurantMenusXML.add(menu);
    }
    public void addFoodItemToDailyMenu(FoodItem foodItem) {
        resDailyMenu.add(foodItem);
    }

    //This method is for Array Adapters to display wanted information.
    //In this case we want to show Restaurants name.
    @Override
    public String toString() {
        String temp;
        temp = this.name;
        return temp;
    }
}
