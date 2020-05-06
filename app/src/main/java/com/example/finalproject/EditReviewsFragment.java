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
    private Button cancelButton;
    private FoodReview selectedOwnReview;
    ParseClass parseClass = ParseClass.getInstance();

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
        writtenReview = (EditText) v.findViewById(R.id.writtenReview);
        cancelButton = (Button)v.findViewById(R.id.review_edit_cancel_button2);

        //getting selected review form the bundle
        try{
            informationBundle = getArguments();
            selectedOwnReview = (FoodReview) informationBundle.getSerializable("reviewKey");
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        //setting old review information
        getOldReviewInformation();

        //setting new review information that user has inputted, setting review as not published and switching fragment
        saveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReviewInformation();

                //setting review as not published
                selectedOwnReview.setPublished(false);

                //modifying xml file based new review values
                parseClass.modifyRestaurantReviewXmlFile(getContext(), selectedOwnReview);


                Toast.makeText(getContext(),getResources().getString(R.string.toast_saveReviewDraft),Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OwnReviewsFragment()).commit();
            }
        });


        //publishing the review and saving it as published
        saveAndPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checks if user has given some kind of feedback
                if ((tasteRatingBar.getRating() == 0)
                        && (appearanceRatingBar.getRating() == 0)
                        &&(textureRatingBar.getRating() == 0)
                        && (writtenReview.getText().toString().trim().equals(""))) {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_reviewScoreAndTextEmpty), Toast.LENGTH_SHORT).show();
                } else {
                    setReviewInformation();
                    selectedOwnReview.setPublished(true);
                    parseClass.modifyRestaurantReviewXmlFile(getContext(), selectedOwnReview);
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_reviewPublished), Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OwnReviewsFragment()).commit();
                }
            }
        });

        //removing review and switching fragment
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseClass.removeReviewFromXml(getContext(),selectedOwnReview);
                Toast.makeText(getContext(),getResources().getString(R.string.toast_reviewRemovedUser),Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OwnReviewsFragment()).commit();
            }
        });

        //going back to previous fragment
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OwnReviewsFragment()).commit();
            }
        });

        return v;
    }

    //setting new review information
    private void setReviewInformation(){
        selectedOwnReview.setReviewText(writtenReview.getText().toString());
        selectedOwnReview.setTasteScore(tasteRatingBar.getRating());
        selectedOwnReview.setLookScore(appearanceRatingBar.getRating());
        selectedOwnReview.setTextureScore(textureRatingBar.getRating());
        selectedOwnReview.setAverageScore();
    }

    //setting old review information
    private void getOldReviewInformation(){
        tasteRatingBar.setRating(selectedOwnReview.getTasteScore());
        textureRatingBar.setRating(selectedOwnReview.getTextureScore());
        appearanceRatingBar.setRating(selectedOwnReview.getLookScore());
        writtenReview.setText(selectedOwnReview.getReviewText());
        foodInfoWindow.setText(selectedOwnReview.getFoodName());
    }

}


