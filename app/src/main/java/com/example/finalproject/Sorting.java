package com.example.finalproject;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Sorting {


    public static ArrayList<FoodReview> sortByDate(ArrayList<FoodReview> originalList) {
        Collections.sort(originalList, new Comparator<FoodReview>() {
            @Override
            public int compare(FoodReview foodReview, FoodReview t1) {
                return foodReview.getDate().compareTo(t1.getDate());
            }
        });
        return originalList;
    }

    public static ArrayList<FoodReview> sortByFood(ArrayList<FoodReview> originalList) {
        Collections.sort(originalList, new Comparator<FoodReview>() {
            @Override
            public int compare(FoodReview foodReview, FoodReview t1) {
                return foodReview.getFoodName().compareTo(t1.getFoodName());
            }
        });
        return originalList;
    }
    public static ArrayList<FoodReview> sortByScore(ArrayList<FoodReview> originalList) {
        Collections.sort(originalList, new Comparator<FoodReview>() {
            @Override
            public int compare(FoodReview foodReview, FoodReview t1) {
                String averageScore1 = String.valueOf(foodReview.getAverageScore());
                String averageScore2 = String.valueOf(t1.getAverageScore());
                return averageScore1.compareTo(averageScore2);
            }
        });
        return originalList;
    }
}
