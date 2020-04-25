package com.example.finalproject;

public class FoodItem {
    private String name;
    private String price;
    private int id;
    public FoodItem(){

    }
    public FoodItem(String name, String price, int id){
        this.name = name;
        this.price = price;
        this.id = id;

    }


    @Override
    public String toString() {
        String temp;
        temp = this.name + "\n" + this.price;
        return temp;
    }
}
