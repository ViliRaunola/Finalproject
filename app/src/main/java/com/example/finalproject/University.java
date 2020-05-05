package com.example.finalproject;

import java.util.ArrayList;

public class University{
    private String name;
    private String id;
    ArrayList<String> restaurantsXML = new ArrayList<>();
    private String info;

    University(String name, String id, String info){
        this.name = name;
        this.id = id;
        this.info = info;
    }

    void addToRestaurants(String restaurant){
        restaurantsXML.add(restaurant);
    }

    public String getInfo() {
        return info;
    }

    /*
    This "toString" method is for array adapter to show selected string.
    In this case University's name.
     */
    @Override
    public String toString() {
        String temp;
        temp = this.name;
        return temp;
    }
}
