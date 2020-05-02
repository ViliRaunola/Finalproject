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

public class EditReviewsFragment extends Fragment {
    private RatingBar tasteRatingBar;
    private RatingBar textureRatingBar;
    private RatingBar appearanceRatingBar;
    private TextView foodInfoWindow;
    private EditText writtenReview;
    private Button saveReviewButton;
    private Button saveAndPublishButton;
    private Button removeButton;
    private Bundle informationBundle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_review, container, false);
        tasteRatingBar = (RatingBar)v.findViewById(R.id.tasteRatingBar);
        textureRatingBar = (RatingBar)v.findViewById(R.id.textureRatingBar);
        appearanceRatingBar = (RatingBar)v.findViewById(R.id.appearanceRatingBar);
        foodInfoWindow = (TextView)v.findViewById(R.id.foodInfoWindow);
        saveReviewButton = (Button)v.findViewById(R.id.saveButtonEditReview);
        saveAndPublishButton = (Button)v.findViewById(R.id.saveAndPublishEditReview);
        removeButton = (Button)v.findViewById(R.id.removeButtonEditReview);

        informationBundle = getArguments();
        FoodReview selectedOwnReview = (FoodReview) informationBundle.getSerializable("reviewKey");


        tasteRatingBar.setRating(selectedOwnReview.getTasteScore());



        saveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Your review was saved",Toast.LENGTH_SHORT).show();
            }
        });
        saveAndPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Your review was saved and published",Toast.LENGTH_SHORT).show();
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Your review was removed",Toast.LENGTH_SHORT).show();
            }
        });



        return v;
    }

}
