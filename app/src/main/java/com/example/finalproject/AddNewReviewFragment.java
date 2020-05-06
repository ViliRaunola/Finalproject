package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddNewReviewFragment extends Fragment {
    private RatingBar tasteRatingBar;
    private RatingBar textureRatingBar;
    private RatingBar appearanceRatingBar;
    private TextView foodInfoWindow;
    private EditText writtenReview;
    private Button saveReviewButton;
    private Button saveAndPublishButton;
    private Button cancelButton;
    private Bundle informationBundle;
    private FoodItem selectedFood;
    private String selectedRestaurantName;
    private FoodReview newOwnReview;
    private ParseClass parseClass = ParseClass.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_new_review, container, false);

        tasteRatingBar = (RatingBar)v.findViewById(R.id.tasteRatingbar_add_new_review);
        textureRatingBar = (RatingBar)v.findViewById(R.id.textureRatingbar_add_new_review);
        appearanceRatingBar = (RatingBar)v.findViewById(R.id.appearenceRatingbar_add_new_review);
        foodInfoWindow = (TextView) v.findViewById(R.id.foodInfoWindow_add_new_review);
        writtenReview = (EditText)v.findViewById(R.id.writtenReview_add_new_review);
        saveAndPublishButton = (Button)v.findViewById(R.id.addreview_button_add_new_review);
        saveReviewButton = (Button)v.findViewById(R.id.save_button_add_new_review);
        cancelButton = (Button)v.findViewById(R.id.cancel_button_add_new_review);

        //getting data from food reviews page
        try{
            informationBundle = getArguments();
            selectedFood = (FoodItem) informationBundle.getSerializable("FoodKey");
            selectedRestaurantName = (String) informationBundle.getString("resKey");
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        //setting food name to text field
        foodInfoWindow.setText(selectedFood.getName());

        //setting new review information and saving it to our own reviews as not published
        saveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReviewInformation();

                //setting review as not published
                newOwnReview.setPublished(false);

                //setting id for the review
                newOwnReview.setReviewId(String.valueOf(parseClass.getBiggestReviewId()));

                //populate all reviews list to get already existing reviews
                parseClass.parseRestaurantReviews(selectedRestaurantName, getContext());
                parseClass.getAllReviews().add(newOwnReview);

                //modifies restaurant file by adding the new review
                parseClass.modifyRestaurantReviewXmlFile(getContext(), newOwnReview);

                Toast.makeText(getContext(),getResources().getString(R.string.toast_saveReviewDraft),Toast.LENGTH_SHORT).show();

                //going to Restaurant Menu page
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UniversityFragment()).commit();
            }
        });

        //checking that user has given feedback and saving the review and publishing it
        saveAndPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking that user has given some kind of feedback
                if ((tasteRatingBar.getRating() == 0)
                && (appearanceRatingBar.getRating() == 0)
                &&(textureRatingBar.getRating() == 0)
                && (writtenReview.getText().toString().trim().equals(""))) {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_reviewScoreAndTextEmpty), Toast.LENGTH_SHORT).show();
                } else {
                    //setting review information
                    setReviewInformation();

                    //setting review as published
                    newOwnReview.setPublished(true);

                    //setting id for the review
                    newOwnReview.setReviewId(String.valueOf(parseClass.getBiggestReviewId()));

                    //populate all reviews list to get already existing reviews
                    parseClass.parseRestaurantReviews(selectedRestaurantName, getContext());
                    parseClass.getAllReviews().add(newOwnReview);

                    //modifies restaurant file by adding the new review
                    parseClass.modifyRestaurantReviewXmlFile(getContext(), newOwnReview);

                    Toast.makeText(getContext(), getResources().getString(R.string.toast_reviewPublished), Toast.LENGTH_SHORT).show();

                    //going to Restaurant Menu page
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UniversityFragment()).commit();
                }
            }
        });

        //going back to previous fragment
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return v;
    }

    //creating new foodReview object and setting information to it
    private void setReviewInformation(){
        newOwnReview = new FoodReview();

        //These come from FoodItem from bundle
        newOwnReview.setFoodId(selectedFood.getId());
        newOwnReview.setFoodName(selectedFood.getName());

        //This comes from bundle also
        newOwnReview.setRestaurant(selectedRestaurantName);

        newOwnReview.setReviewText(writtenReview.getText().toString());
        newOwnReview.setTasteScore(tasteRatingBar.getRating());
        newOwnReview.setLookScore(appearanceRatingBar.getRating());
        newOwnReview.setTextureScore(textureRatingBar.getRating());
        newOwnReview.setUserId(String.valueOf(User.getInstance().getUserID()));
        newOwnReview.setDate(DateClass.getInstance().getCurrentDate());
        newOwnReview.setVoteScore(0);
        newOwnReview.setAverageScore();
    }



}
