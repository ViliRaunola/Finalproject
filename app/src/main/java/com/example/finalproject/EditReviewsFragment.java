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
    private Context context;


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
                modifyRestaurantReviewXmlFile();
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
        }catch (IOException e) {//TODO ADD REAL EXCEPTION
            e.printStackTrace();
        }

        /*

        try {
            System.out.println("TÄSTÄ ALKAAAAAA modifyRestaurantReviewXmlFile &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            //TODO lisää osa parse luokkaan
            FileOutputStream fos = new FileOutputStream( getContext().getFilesDir() +"/reviews/" + selectedOwnReview.getRestaurant() + "_Reviews.xml");
            FileOutputStream fileos = getContext().openFileOutput(selectedOwnReview.getRestaurant() + "_Reviews", Context.MODE_PRIVATE);

            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "reviews");
            for (FoodReview review : allReviews) {

                if (!review.getReviewId().equals(selectedOwnReview.getReviewId())) {
                    xmlSerializer.startTag(null, "review");

                    xmlSerializer.startTag(null, "reviewId");
                    xmlSerializer.text(review.getReviewId());
                    xmlSerializer.endTag(null, "reviewId");

                    xmlSerializer.startTag(null, "foodId");
                    xmlSerializer.text(review.getFoodId());
                    xmlSerializer.endTag(null, "foodId");

                    xmlSerializer.startTag(null, "foodName");
                    xmlSerializer.text(review.getFoodName());
                    xmlSerializer.endTag(null, "foodName");

                    xmlSerializer.startTag(null, "userId");
                    xmlSerializer.text(review.getUserId());
                    xmlSerializer.endTag(null, "userId");

                    xmlSerializer.startTag(null, "tasteScore");
                    xmlSerializer.text(String.valueOf(review.getTasteScore()));
                    xmlSerializer.endTag(null, "tasteScore");

                    xmlSerializer.startTag(null, "lookScore");
                    xmlSerializer.text(String.valueOf(review.getLookScore()));
                    xmlSerializer.endTag(null, "lookScore");

                    xmlSerializer.startTag(null, "textureScore");
                    xmlSerializer.text(String.valueOf(review.getTextureScore()));
                    xmlSerializer.endTag(null, "textureScore");

                    xmlSerializer.startTag(null, "reviewText");
                    xmlSerializer.text(review.getReviewText());
                    xmlSerializer.endTag(null, "reviewText");

                    xmlSerializer.startTag(null, "date");
                    xmlSerializer.text(review.getDateString());
                    xmlSerializer.endTag(null, "date");

                    xmlSerializer.startTag(null, "published");
                    xmlSerializer.text(review.getPublished());
                    xmlSerializer.endTag(null, "published");

                    xmlSerializer.endTag(null, "review");
                } else {
                    xmlSerializer.startTag(null, "review");

                    xmlSerializer.startTag(null, "reviewId");
                    xmlSerializer.text(selectedOwnReview.getReviewId());
                    xmlSerializer.endTag(null, "reviewId");

                    xmlSerializer.startTag(null, "foodId");
                    xmlSerializer.text(selectedOwnReview.getFoodId());
                    xmlSerializer.endTag(null, "foodId");

                    xmlSerializer.startTag(null, "foodName");
                    xmlSerializer.text(selectedOwnReview.getFoodName());
                    xmlSerializer.endTag(null, "foodName");

                    xmlSerializer.startTag(null, "userId");
                    xmlSerializer.text(selectedOwnReview.getUserId());
                    xmlSerializer.endTag(null, "userId");

                    xmlSerializer.startTag(null, "tasteScore");
                    xmlSerializer.text(String.valueOf(selectedOwnReview.getTasteScore()));
                    xmlSerializer.endTag(null, "tasteScore");

                    xmlSerializer.startTag(null, "lookScore");
                    xmlSerializer.text(String.valueOf(selectedOwnReview.getLookScore()));
                    xmlSerializer.endTag(null, "lookScore");

                    xmlSerializer.startTag(null, "textureScore");
                    xmlSerializer.text(String.valueOf(selectedOwnReview.getTextureScore()));
                    xmlSerializer.endTag(null, "textureScore");

                    xmlSerializer.startTag(null, "reviewText");
                    xmlSerializer.text(selectedOwnReview.getReviewText());
                    xmlSerializer.endTag(null, "reviewText");

                    xmlSerializer.startTag(null, "date");
                    xmlSerializer.text(selectedOwnReview.getDateString());
                    xmlSerializer.endTag(null, "date");

                    xmlSerializer.startTag(null, "published");
                    xmlSerializer.text(selectedOwnReview.getPublished());
                    xmlSerializer.endTag(null, "published");

                    xmlSerializer.endTag(null, "review");
                }
            }
            xmlSerializer.endTag(null, "reviews");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

         */
    }
}


