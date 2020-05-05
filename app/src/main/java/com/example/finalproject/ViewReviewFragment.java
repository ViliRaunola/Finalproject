package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.util.ArrayList;

public class ViewReviewFragment extends Fragment {

    private RatingBar tasteRatingBar;
    private RatingBar textureRatingBar;
    private RatingBar appearanceRatingBar;
    private TextView foodInfoWindow;
    private TextView writtenReview;
    private Button removeButton;
    private Button hideButton;
    private Button downVoteButton;
    private Button upVoteButton;
    private Bundle informationBundle;
    private FoodReview selectedFoodReview;
    private TextView voteScoreTextView;
    private Boolean allReadyUpVoted;
    private Boolean allReadyDownVoted;
    User user = User.getInstance();
    ParseClass parseClass = ParseClass.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_viewreview, container, false);

        tasteRatingBar = (RatingBar)v.findViewById(R.id.tasteRatingBar_viewReview);
        textureRatingBar = (RatingBar)v.findViewById(R.id.textureRatingBar_viewReview);
        appearanceRatingBar = (RatingBar)v.findViewById(R.id.appearanceRatingBar_viewReview);
        foodInfoWindow = (TextView)v.findViewById(R.id.foodInfoWindow_viewReview);
        writtenReview = (TextView)v.findViewById(R.id.writtenReview_viewReview);
        voteScoreTextView = (TextView) v.findViewById(R.id.voteScoreTextView);
        removeButton = (Button)v.findViewById(R.id.deleteReviewButton_viewReview);
        hideButton = (Button)v.findViewById(R.id.hideReviewButton_viewReview);
        downVoteButton = (Button) v.findViewById(R.id.downVoteButton);
        upVoteButton = (Button) v.findViewById(R.id.upVoteButton);

        /*This is for making sure up vote and down vote buttons are either disabled or enabled depending
        on the user's previous vote.
        */
        allReadyDownVoted = false;
        allReadyUpVoted = false;
        try{
            informationBundle = getArguments();
            selectedFoodReview = (FoodReview) informationBundle.getSerializable("FoodReviewKey");
        }catch (Exception e){//TODO ADD REAL EXCEPTION
            e.printStackTrace();
        }
        System.out.println(allReadyDownVoted+"all ready downvoted");
        System.out.println(allReadyUpVoted+"Allreadyupvotes");
        allReadyDownVoted = checkVoteList(user.getDownVotedList());
        allReadyUpVoted = checkVoteList(user.getUpVotedList());
        if (allReadyUpVoted) {
            upVoteButton.setEnabled(false);
        }
        if (allReadyDownVoted) {
            downVoteButton.setEnabled(false);
        }
        foodInfoWindow.setText(selectedFoodReview.getFoodName());
        voteScoreTextView.setText(String.valueOf(selectedFoodReview.getVoteScore()));

        //Sets the values for all the fields
        getOldReviewInformation();

        //Makes the button disabled if user is not an admin user
        removeButton.setEnabled(user.getIsAdminUser());
        hideButton.setEnabled(user.getIsAdminUser());

        //Checks if the current is user. If true it shows the buttons and makes them functional
        if(user.getIsAdminUser()){
            removeButton.setVisibility(View.VISIBLE);
            hideButton.setVisibility(View.VISIBLE);
        }else {
            removeButton.setVisibility(View.INVISIBLE);
            hideButton.setVisibility(View.INVISIBLE);
        }

        //When this button is pressed the selected review's "published"-boolean is set to false.
        //Writes the review back to file with changed boolean
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFoodReview.setPublished(false);
                parseClass.parseRestaurantReviews(selectedFoodReview.getRestaurant(), getContext());
                parseClass.modifyRestaurantReviewXmlFile(getContext(), selectedFoodReview);
                Toast.makeText(getContext(),"You have hid a review",Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });


        //When pressed "DELETE-BUTTON" removes the review that the admin is viewing
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseClass.parseRestaurantReviews(selectedFoodReview.getRestaurant(), getContext());
                parseClass.removeReviewFromXml(getContext(), selectedFoodReview);
                Toast.makeText(getContext(),"You have removed a review",Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });


        /*
        Down vote and up vote buttons add or remove review's id from user's up vote or down vote list.
        They also rewrite user's json file to save what the user has up or down voted.
         */
        downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFoodReview.changeVoteScore(-1);
                voteScoreTextView.setText(String.valueOf(selectedFoodReview.getVoteScore()));

                if (checkVoteList(user.getUpVotedList())){
                    user.getUpVotedList().remove(selectedFoodReview.getReviewId());
                    downVoteButton.setEnabled(true);
                    upVoteButton.setEnabled(true);
                }else {
                    downVoteButton.setEnabled(false);
                    upVoteButton.setEnabled(true);
                    user.getDownVotedList().add(selectedFoodReview.getReviewId());
                }

                try {
                    parseClass.writeUserJson(getContext(), User.getInstance());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                parseClass.parseRestaurantReviews(selectedFoodReview.getRestaurant(), getContext());
                parseClass.modifyRestaurantReviewXmlFile(getContext(),selectedFoodReview);
            }
        });

        upVoteButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFoodReview.changeVoteScore(1);
                voteScoreTextView.setText(String.valueOf(selectedFoodReview.getVoteScore()));

                if (checkVoteList(user.getDownVotedList())){
                    upVoteButton.setEnabled(true);
                    downVoteButton.setEnabled(true);
                    user.getDownVotedList().remove(selectedFoodReview.getReviewId());
                }else {
                    downVoteButton.setEnabled(true);
                    upVoteButton.setEnabled(false);
                    user.getUpVotedList().add(selectedFoodReview.getReviewId());
                }
                try {
                    parseClass.writeUserJson(getContext(), User.getInstance());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                parseClass.parseRestaurantReviews(selectedFoodReview.getRestaurant(), getContext());
                parseClass.modifyRestaurantReviewXmlFile(getContext(),selectedFoodReview);
            }
        }));


        return v;
    }
    /*
    Sets values to the fragments elements from current Food Review.
     */
    private void getOldReviewInformation(){
        tasteRatingBar.setRating(selectedFoodReview.getTasteScore());
        textureRatingBar.setRating(selectedFoodReview.getTextureScore());
        appearanceRatingBar.setRating(selectedFoodReview.getLookScore());
        writtenReview.setText(selectedFoodReview.getReviewText());
        foodInfoWindow.setText(selectedFoodReview.getFoodName());
    }

    /*
    Goes through given list and returns true if current Food Review's Id matches the
    value from ArrayList.
     */
    public Boolean checkVoteList(ArrayList<String> list) {
        Boolean booleanCheck = false;
        for (String s : list){
            if (s.equals(selectedFoodReview.getReviewId())){
                booleanCheck = true;
            }
        }
        return booleanCheck;
    }

}
