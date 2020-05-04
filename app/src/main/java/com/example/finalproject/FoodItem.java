package com.example.finalproject;

import java.io.Serializable;

public class FoodItem implements Serializable {
    private String name;
    private String price;
    private String id;
    private int day;
    public FoodItem(){

    }
    public FoodItem(String name, String price, String id, int day){
        this.name = name;
        this.price = price;
        this.id = id;
        this.day = day;

    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getDay(){
        return this.day;
    }

    @Override
    public String toString() {
        String temp;
        temp = this.name + "\n" + this.price;
        return temp;
    }
}
