package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoodReviewsFragment extends Fragment {
    private Button addReviewButton;
    private RatingBar overallRatingBar;
    private ListView allReviewsListView;
    private TextView foodNameTextView;
    private Boolean userReviewBoolean;
    private FoodItem selectedFood;
    private float overallRating;
    private int reviewCounter = 0;
    private Bundle informationBundle;
    private String selectedRestaurantName;
    private String dayOfFoodString;
    private Spinner sortingSpinner;
    private List<String> sortingList = new ArrayList<String>();
    private ArrayList<FoodReview> reviewsForFood = new ArrayList<FoodReview>();

    private ParseClass parseClass = ParseClass.getInstance();
    private User user = User.getInstance();
    private DateClass dateClass = DateClass.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.food_reviews_fragment, container, false);

        addReviewButton = (Button) v.findViewById(R.id.addReviewButton);
        overallRatingBar = (RatingBar) v.findViewById(R.id.overalLratingBar_foodReviewsFragment);
        allReviewsListView = (ListView) v.findViewById(R.id.allReviews_foodReviewsFragment);
        foodNameTextView = (TextView) v.findViewById(R.id.foodNameTextView);
        sortingSpinner = (Spinner) v.findViewById(R.id.sortingSpinner_foorReviewsFragment);

        //making sure that values are correct
        overallRating = 0;
        reviewCounter = 0;
        reviewsForFood.clear();
        sortingList.clear();
        userReviewBoolean = false;

        //adding sorting strings to sorting list and setting sorting spinner
        sortingList.add(getResources().getString(R.string.ownReviewsView_date));
        sortingList.add(getResources().getString(R.string.ownReviewsView_overallScore));
        sortingList.add(getResources().getString(R.string.ownReviewsView_vote));
        ArrayAdapter<String> sortingArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sortingList);
        sortingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(sortingArrayAdapter);

        //getting values from the bundle
        try{
            informationBundle = getArguments();
            selectedFood = (FoodItem) informationBundle.getSerializable("FoodKey");
            selectedRestaurantName = (String) informationBundle.getString("resKey");
            dayOfFoodString = (String) informationBundle.getString("dateKey");
            parseClass.parseRestaurantReviews(selectedRestaurantName, getContext());
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }

        //setting food name to text field
        foodNameTextView.setText(selectedFood.getName());

        for (FoodReview r : parseClass.getAllReviews()) {
            // reads through list of all reviews for restaurant. If a review has same foodId as the selected food, then the review is shown in the ListView.
            if (selectedFood.getId().equals(r.getFoodId()) && (r.getPublishedBoolean())) {
                reviewsForFood.add(r);
                overallRating += r.getAverageScore();
                reviewCounter += 1;
                // Checks if user has already made a review for currently selected food
                if (r.getUserId().equals(String.valueOf(user.getUserID()))) {
                    userReviewBoolean = true;
                }
                // Checks if user has already made a review for currently selected food but hasn't published it yet. This way user can't make multiple reviews for the same food through own reviews.
            } else if (selectedFood.getId().equals(r.getFoodId()) && !(r.getPublishedBoolean())) {
                if (r.getUserId().equals(String.valueOf(user.getUserID()))) {
                    userReviewBoolean = true;
                }
            }
        }

        //calculates overall rating among all reviews for the selected food.
        overallRating = overallRating / reviewCounter;
        overallRatingBar.setRating(overallRating);

        //setting reviews list view
        if (reviewsForFood.size() > 0) {
            ArrayAdapter<FoodReview> arrayAdapter = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, reviewsForFood);
            allReviewsListView.setAdapter(arrayAdapter);
        }

        //sorting spinner listener
        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        //https://www.youtube.com/watch?v=Mguw_TQBExo how to use Collections.sort
                        //sort by date using Collections.sort for current food reviews
                        Sorting.sortByDate(reviewsForFood);

                        //Reverses the list
                        Collections.reverse(reviewsForFood);

                        //setting new adapter for current food reviews
                        ArrayAdapter<FoodReview> arrayAdapterDate = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1,  reviewsForFood);
                        allReviewsListView.setAdapter(arrayAdapterDate);
                        break;

                    case 1:
                        //https://stackoverflow.com/questions/9941890/sorting-arraylist-of-objects-by-float how to sort by float
                        //sort by overall score using Collections.sort for current food reviews
                        Sorting.sortByScore(reviewsForFood);

                        //reverse the list
                        Collections.reverse(reviewsForFood);

                        //setting new adapter for current food reviews
                        ArrayAdapter<FoodReview> arrayAdapterScore = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1,  reviewsForFood);
                        allReviewsListView.setAdapter(arrayAdapterScore);
                        break;
                    case 2:
                        //https://www.youtube.com/watch?v=Mguw_TQBExo how to use Collections.sort
                        //sort by date using Collections.sort for current foods votes
                        Sorting.sortByVote(reviewsForFood);

                        //reverse the list
                        Collections.reverse(reviewsForFood);

                        //setting new adapter for current food reviews
                        ArrayAdapter<FoodReview> arrayAdapterVote = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1,  reviewsForFood);
                        allReviewsListView.setAdapter(arrayAdapterVote);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //adding new review
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks if user has already made a review for currently selected food
                if (userReviewBoolean) {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_multipleReviewsForOneFood), Toast.LENGTH_SHORT).show();
                } else {
                    //Checks if user is trying to make a review for a food that has not been served yet.
                    //https://developer.android.com/reference/android/app/FragmentTransaction for fragment transaction
                    if (dateClass.compareDates(dayOfFoodString)) {
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
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.toast_foodNotServedYet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //changing to new fragment for viewing the review
        allReviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //https://developer.android.com/reference/android/app/FragmentTransaction for fragment transaction
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ViewReviewFragment viewReviewFragment = new ViewReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("FoodReviewKey", reviewsForFood.get(position));
                viewReviewFragment.setArguments(bundle);
                ft.replace(R.id.fragment_container, viewReviewFragment);
                ft.addToBackStack("fragment_viewreview");
                ft.commit();
            }
        });
        return v;
    }

    //clearing all reviews list and setting rating bar
    @Override
    public void onResume() {
        super.onResume();
        parseClass.getAllReviews().clear();
        foodNameTextView.invalidate();
        overallRatingBar.setRating(overallRating);
    }
}
