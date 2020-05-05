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

public class ViewReviewFragment extends Fragment {

    private RatingBar tasteRatingBar;
    private RatingBar textureRatingBar;
    private RatingBar appearanceRatingBar;
    private TextView foodInfoWindow;
    private TextView writtenReview;
    private Button removeButton;
    private Button hideButton;

    private Bundle informationBundle;
    private FoodReview selectedFoodReview;

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
        removeButton = (Button)v.findViewById(R.id.deleteReviewButton_viewReview);
        hideButton = (Button)v.findViewById(R.id.hideReviewButton_viewReview);


        try{
            informationBundle = getArguments();
            selectedFoodReview = (FoodReview) informationBundle.getSerializable("FoodReviewKey");
        }catch (Exception e){//TODO ADD REAL EXCEPTION
            e.printStackTrace();
        }




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
                Toast.makeText(getContext(),"You have hid this review",Toast.LENGTH_SHORT).show();
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


        return v;
    }

    private void getOldReviewInformation(){
        tasteRatingBar.setRating(selectedFoodReview.getTasteScore());
        textureRatingBar.setRating(selectedFoodReview.getTextureScore());
        appearanceRatingBar.setRating(selectedFoodReview.getLookScore());
        writtenReview.setText(selectedFoodReview.getReviewText());
        foodInfoWindow.setText(selectedFoodReview.getFoodName());
    }
}
