package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FoodReviewsFragment extends Fragment {
    private Button addReviewButton;
    private RatingBar overallRatingBar;
    private ListView allReviewsListView;
    private TextView foodNameTextView;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.food_reviews_fragment, container, false);
        addReviewButton = (Button)v.findViewById(R.id.addReviewButton);
        overallRatingBar = (RatingBar)v.findViewById(R.id.overalLratingBar_foodReviewsFragment);
        allReviewsListView = (ListView)v.findViewById(R.id.listViewFood);
        foodNameTextView = (TextView)v.findViewById(R.id.foodNameTextView);

        return v;
    }
}
