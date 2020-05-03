package com.example.finalproject;

import android.content.Context;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


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
    private FoodReview selectedOwnReview;
    private ArrayList<FoodReview> allReviews;


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

        try{
            informationBundle = getArguments();
            selectedOwnReview = (FoodReview) informationBundle.getSerializable("reviewKey");
            allReviews = (ArrayList<FoodReview>) informationBundle.getSerializable("allReviews");
        }catch (Exception e){//TODO ADD REAL EXCEPTION
            e.printStackTrace();
        }

        getOldReviewInformation();

        saveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReviewInformation();
                selectedOwnReview.setPublished(false);
                modifyRestaurantReviewXmlFile();
                Toast.makeText(getContext(),"Your review was saved",Toast.LENGTH_SHORT).show();
            }
        });
        saveAndPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedOwnReview.setPublished(true);
                modifyRestaurantReviewXmlFile();
                Toast.makeText(getContext(),"Your review was saved and published",Toast.LENGTH_SHORT).show();
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeReviewFromXml();
                Toast.makeText(getContext(),"Your review was removed",Toast.LENGTH_SHORT).show();
            }
        });



        return v;
    }

    private void setReviewInformation(){
        selectedOwnReview.setReviewText(writtenReview.getText().toString());
        selectedOwnReview.setTasteScore(tasteRatingBar.getRating());
        selectedOwnReview.setLookScore(appearanceRatingBar.getRating());
        selectedOwnReview.setTextureScore(textureRatingBar.getRating());
        selectedOwnReview.setAverageScore();
    }

    private void getOldReviewInformation(){
        tasteRatingBar.setRating(selectedOwnReview.getTasteScore());
        textureRatingBar.setRating(selectedOwnReview.getTextureScore());
        appearanceRatingBar.setRating(selectedOwnReview.getLookScore());
        writtenReview.setText(selectedOwnReview.getReviewText());
        foodInfoWindow.setText(selectedOwnReview.getFoodName());
    }

    //https://stackoverflow.com/questions/17022221/openfileoutput-how-to-create-files-outside-the-data-data-path
    //Teacher's coding video
    //Rewrites the specific restaurant review folder with new information for the selected review
    public void modifyRestaurantReviewXmlFile() {

        OutputStreamWriter osw = null;
        String s;
        try {
            File file = new File(getContext().getFilesDir() +"/reviews/" + selectedOwnReview.getRestaurant() + "_Reviews.xml");
            FileOutputStream fos = new FileOutputStream(file);

            s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<reviews>";
            fos.write(s.getBytes());

            for (FoodReview review : allReviews) {
                System.out.println(allReviews.size());
                if (review.getReviewId().equals(selectedOwnReview.getReviewId())){
                    System.out.println("IFFIN SISÄLLÄ ##############");
                    System.out.println(selectedOwnReview.getAverageScore());
                    s = "<review>\n" +
                            "        <reviewId>" + selectedOwnReview.getReviewId() + "</reviewId>\n" +
                            "        <foodId>" + selectedOwnReview.getFoodId() + "</foodId>\n" +
                            "        <foodName>" + selectedOwnReview.getFoodName() + "</foodName>\n" +
                            "        <userId>" + selectedOwnReview.getUserId() + "</userId>\n" +
                            "        <tasteScore>" + selectedOwnReview.getTasteScore() + "</tasteScore>\n" +
                            "        <lookScore>" + selectedOwnReview.getLookScore() + "</lookScore>\n" +
                            "        <textureScore>" + selectedOwnReview.getTextureScore() + "</textureScore>\n" +
                            "        <reviewText>" + selectedOwnReview.getReviewText() + "</reviewText>\n" +
                            "        <date>" + selectedOwnReview.getDateString() + "</date>\n" +
                            "        <published>" + selectedOwnReview.getPublished() + "</published>\n" +
                            "    </review>";
                }else {
                    System.out.println("ELSEN SISÄLLÄ ##############");
                    s = "<review>\n" +
                            "        <reviewId>" + review.getReviewId() + "</reviewId>\n" +
                            "        <foodId>" + review.getFoodId() + "</foodId>\n" +
                            "        <foodName>" + review.getFoodName() + "</foodName>\n" +
                            "        <userId>" + review.getUserId() + "</userId>\n" +
                            "        <tasteScore>" + review.getTasteScore() + "</tasteScore>\n" +
                            "        <lookScore>" + review.getLookScore() + "</lookScore>\n" +
                            "        <textureScore>" + review.getTextureScore() + "</textureScore>\n" +
                            "        <reviewText>" + review.getReviewText() + "</reviewText>\n" +
                            "        <date>" + review.getDateString() + "</date>\n" +
                            "        <published>" + review.getPublished() + "</published>\n" +
                            "    </review>";
                }
                fos.write(s.getBytes());
            }
            s = "\n</reviews>";
            fos.write(s.getBytes());
            fos.close();
            allReviews.clear();
        }catch (IOException e) {//TODO ADD REAL EXCEPTION
            e.printStackTrace();
        }
    }

    public void removeReviewFromXml() {

        OutputStreamWriter osw = null;
        String s;
        try {
            File file = new File(getContext().getFilesDir() +"/reviews/" + selectedOwnReview.getRestaurant() + "_Reviews.xml");
            FileOutputStream fos = new FileOutputStream(file);

            s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<reviews>";
            fos.write(s.getBytes());

            for (FoodReview review : allReviews) {
                System.out.println(allReviews.size());
                if (review.getReviewId().equals(selectedOwnReview.getReviewId())){
                    s = "";
                }else {
                    System.out.println("ELSEN SISÄLLÄ ##############");
                    s = "<review>\n" +
                            "        <reviewId>" + review.getReviewId() + "</reviewId>\n" +
                            "        <foodId>" + review.getFoodId() + "</foodId>\n" +
                            "        <foodName>" + review.getFoodName() + "</foodName>\n" +
                            "        <userId>" + review.getUserId() + "</userId>\n" +
                            "        <tasteScore>" + review.getTasteScore() + "</tasteScore>\n" +
                            "        <lookScore>" + review.getLookScore() + "</lookScore>\n" +
                            "        <textureScore>" + review.getTextureScore() + "</textureScore>\n" +
                            "        <reviewText>" + review.getReviewText() + "</reviewText>\n" +
                            "        <date>" + review.getDateString() + "</date>\n" +
                            "        <published>" + review.getPublished() + "</published>\n" +
                            "    </review>";
                }
                fos.write(s.getBytes());
            }
            s = "\n</reviews>";
            fos.write(s.getBytes());
            fos.close();
            allReviews.remove(selectedOwnReview);
        }catch (IOException e) {//TODO ADD REAL EXCEPTION
            e.printStackTrace();
        }
    }
}


