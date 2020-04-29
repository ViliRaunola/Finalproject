package com.example.finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class OwnReviewsFragment extends Fragment {

    private TextView tv;
    private Spinner sortingSpinner;
    private List<String> sortingList = new ArrayList<String>();
    private List<String> reviewsList = new ArrayList<String>();
    private ListView notPublishedReviews;
    private ListView publishedReviews;
    private String selectedSortingMethod;
    EditOwnReviewsFragment editOwnReviewsFragment = new EditOwnReviewsFragment();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_own_reviews, container, false);

        publishedReviews = (ListView)v.findViewById(R.id.listViewPublishedReviews);
        notPublishedReviews = (ListView)v.findViewById(R.id.listViewNotPublishedReviews);
        sortingSpinner = (Spinner)v.findViewById(R.id.ownReviews_sorting_spinner);

        //sorting options
        sortingList.add("Date");
        sortingList.add("Food"); //ruoka;pvm;restaurant;score
        sortingList.add("Restaurant");
        sortingList.add("Average Score");

        //test list
        reviewsList.add("Food:  Lihapullat ja perunamuusi\nDate:  28.4.2020\nRestaurant:  Laseri\nAverage Score:  4.5 stars");
        reviewsList.add("Food:  Lohipullat ja perunamuusi\nDate:  29.4.2020\nRestaurant:  Lut Buffet\nAverage Score:  4.0 stars");
        reviewsList.add("Food:  Lohipullat ja perunamuusi\nDate:  29.4.2020\nRestaurant:  Lut Buffet\nAverage Score:  4.0 stars");
        //arrayadapter for spinner
        ArrayAdapter<String> ap = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sortingList);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(ap);

        //arrayadapter for publishedreviews listview
        ArrayAdapter<String> ap2 = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,reviewsList);
        publishedReviews.setAdapter(ap2);
        notPublishedReviews.setAdapter(ap2);



        //sorting spinner listener
        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               selectedSortingMethod = sortingSpinner.getSelectedItem().toString();
               switch (selectedSortingMethod) {
                   case "Date":
                       Toast.makeText(getContext(), "Sorting by date", Toast.LENGTH_SHORT).show();
                       break;
                   case "Food":
                       Toast.makeText(getContext(), "Sorting by food", Toast.LENGTH_SHORT).show();
                       break;
                   case "Restaurant":
                       Toast.makeText(getContext(), "Sorting by restaurant", Toast.LENGTH_SHORT).show();
                       break;
                   case "Average Score":
                       Toast.makeText(getContext(), "Sorting by average score", Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        publishedReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editOwnReviewsFragment).commit();
                //Intent intent = new Intent(getActivity(),MainActivity.class);
                //startActivity(intent);
            }
        });
        notPublishedReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return v;
    }



}
