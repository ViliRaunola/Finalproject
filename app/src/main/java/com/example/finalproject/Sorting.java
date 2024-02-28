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
        Collections.sort(arrayList, (a,b) -> Float.compare(a.getAverageScore(), b.getAverageScore()));
    }
    //sorting by food using Collections.sort method
    public static void sortByFood(ArrayList<FoodReview> arrayList){
        Collections.sort(arrayList, (a,b) -> a.getFoodName().compareTo(b.getFoodName());
    }
    //sorting by date using Collections.sort method
    public static void sortByDate(ArrayList<FoodReview> arrayList) {
        Collections.sort(arrayList, (a,b) -> a.getDate().compareTo(b.getDate());
    }
    //sorting by vote using Collections.sort method
    public static void sortByVote(ArrayList<FoodReview> arrayList) {
        Collections.sort(arrayList, (a,b) -> Integer.compare(a.getVoteScore(), b.getVoteScore());
    }
}
