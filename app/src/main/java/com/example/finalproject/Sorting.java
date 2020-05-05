package com.example.finalproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* sources:
*https://www.youtube.com/watch?v=Mguw_TQBExo
* https://stackoverflow.com/questions/9941890/sorting-arraylist-of-objects-by-float
*/
public class Sorting {

    //sorting by score using Collections.sort method
    public static void sortByScore(ArrayList<FoodReview> arrayList) {
        Collections.sort(arrayList, new Comparator<FoodReview>() {
            @Override
            public int compare(FoodReview foodReview, FoodReview t1) {
                return Float.compare(foodReview.getAverageScore(), t1.getAverageScore());
            }
        });
    }
    //sorting by food using Collections.sort method
    public static void sortByFood(ArrayList<FoodReview> arrayList){
        Collections.sort(arrayList, new Comparator<FoodReview>() {
            @Override
            public int compare(FoodReview foodReview, FoodReview t1) {
                return foodReview.getFoodName().compareTo(t1.getFoodName());
            }
        });
    }
    //sorting by date using Collections.sort method
    public static void sortByDate(ArrayList<FoodReview> arrayList) {
        Collections.sort(arrayList, new Comparator<FoodReview>() {
            @Override
            public int compare(FoodReview foodReview, FoodReview t1) {
                return foodReview.getDate().compareTo(t1.getDate());
            }
        });
    }
    //sorting by vote using Collections.sort method
    public static void sortByVote(ArrayList<FoodReview> arrayList) {
        Collections.sort(arrayList, new Comparator<FoodReview>() {
            @Override
            public int compare(FoodReview foodReview, FoodReview t1) {
                return Integer.compare(foodReview.getVoteScore(), t1.getVoteScore());
            }
        });
    }
}
