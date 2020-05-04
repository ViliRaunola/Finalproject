package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class FoodReviewsFragment extends Fragment {
    private Button addReviewButton;
    private RatingBar overallRatingBar;
    private ListView allReviewsListView;
    private TextView foodNameTextView;
    private ParseClass parseClass = ParseClass.getInstance();
    private ArrayList<FoodReview> reviewsForFood = new ArrayList<FoodReview>();
    private FoodItem selectedFood;
    float overallRating;
    int reviewCounter = 0;
    private Bundle informationBundle;
    private String selectedRestaurantName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.food_reviews_fragment, container, false);
        addReviewButton = (Button)v.findViewById(R.id.addReviewButton);
        overallRatingBar = (RatingBar)v.findViewById(R.id.overalLratingBar_foodReviewsFragment);
        allReviewsListView = (ListView)v.findViewById(R.id.allReviews_foodReviewsFragment);
        foodNameTextView = (TextView)v.findViewById(R.id.foodNameTextView);




        try{
            informationBundle = getArguments();
            selectedFood = (FoodItem) informationBundle.getSerializable("FoodKey");
            selectedRestaurantName = (String) informationBundle.getString("resKey");
            parseClass.parseRestaurantReviews(selectedRestaurantName , getContext());
        }catch (Exception e){//TODO ADD REAL EXCEPTION
            e.printStackTrace();
        }

        foodNameTextView.setText(selectedFood.getName());

        for (FoodReview r : parseClass.getAllReviews()) {
            if (selectedFood.getId().equals(r.getFoodId()) && (r.getPublishedBoolean())) {
                reviewsForFood.add(r);
                overallRating += r.getAverageScore();
                reviewCounter += 1;
            }
        }

        overallRating = overallRating / reviewCounter;
        overallRatingBar.setRating(overallRating);

        if (reviewsForFood.size() > 0) {
            ArrayAdapter<FoodReview> arrayAdapter = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, reviewsForFood);
            allReviewsListView.setAdapter(arrayAdapter);
        }


        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                AddNewReviewFragment addNewReviewFragment = new AddNewReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("FoodKey", selectedFood);
                bundle.putString("resKey", selectedRestaurantName);
                addNewReviewFragment.setArguments(bundle);
                ft.replace(R.id.fragment_container, addNewReviewFragment);
                ft.addToBackStack("Fragment_add_new_review");
                ft.commit();
            }
        });



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        parseClass.getAllReviews().clear();
    }
}
