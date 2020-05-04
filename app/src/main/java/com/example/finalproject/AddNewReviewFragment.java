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
import androidx.fragment.app.FragmentTransaction;


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
    ParseClass parseClass = ParseClass.getInstance();


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



        try{
            informationBundle = getArguments();
            selectedFood = (FoodItem) informationBundle.getSerializable("FoodKey");
            selectedRestaurantName = (String) informationBundle.getString("resKey");
        }catch (Exception e){//TODO ADD REAL EXCEPTION
            e.printStackTrace();
        }



        saveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReviewInformation();
                newOwnReview.setPublished(false);
                newOwnReview.setReviewId(String.valueOf(parseClass.getBiggestReviewId()));
                parseClass.parseRestaurantReviews(selectedRestaurantName, getContext());
                parseClass.getAllReviews().add(newOwnReview);
                parseClass.modifyRestaurantReviewXmlFile(getContext(), newOwnReview);
                Toast.makeText(getContext(),"Your review was saved",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UniversityFragment()).commit();
            }
        });

        saveAndPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReviewInformation();
                newOwnReview.setPublished(true);
                newOwnReview.setReviewId(String.valueOf(parseClass.getBiggestReviewId()));

                System.out.println(parseClass.getAllReviews() + "Ennen ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤");

                parseClass.parseRestaurantReviews(selectedRestaurantName, getContext());

                parseClass.getAllReviews().add(newOwnReview);
                System.out.println(parseClass.getAllReviews() + "jälkeen");
                parseClass.modifyRestaurantReviewXmlFile(getContext(), newOwnReview);
                Toast.makeText(getContext(),"Your review was saved and published",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UniversityFragment()).commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FoodReviewsFragment()).commit();
            }
        });
        return v;
    }

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
        newOwnReview.setAverageScore();
    }



}
