package com.example.finalproject;

public class FoodItem {
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


    @Override
    public String toString() {
        String temp;
        temp = this.name + "\n" + this.price;
        return temp;
    }
}
