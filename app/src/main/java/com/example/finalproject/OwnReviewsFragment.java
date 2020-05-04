package com.example.finalproject;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class OwnReviewsFragment extends Fragment {

    private TextView tv;
    private Spinner sortingSpinner;
    private Spinner universitySpinner;
    private Spinner restaurantSpinner;
    private List<String> sortingList = new ArrayList<String>();
    private List<String> reviewsList = new ArrayList<String>();
    private ListView notPublishedReviewsListView ;
    private ListView publishedReviewsListView ;
    private String selectedSortingMethod;
    private String selectedRestaurantName;
    View v;
    EditReviewsFragment editReviewsFragment = new EditReviewsFragment();
    User user = User.getInstance();
    ParseClass parseClass = ParseClass.getInstance();
    private Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_own_reviews, container, false);

        context = this.getContext();

        publishedReviewsListView = (ListView) v.findViewById(R.id.listViewPublishedReviews);
        notPublishedReviewsListView = (ListView) v.findViewById(R.id.listViewNotPublishedReviews);
        sortingSpinner = (Spinner) v.findViewById(R.id.ownReviews_sorting_spinner);
        universitySpinner = (Spinner)v.findViewById(R.id.universitySpinner_ownReviews);
        restaurantSpinner = (Spinner)v.findViewById(R.id.restaurantSpinner_ownReviews);


        //clear spinner and list
        sortingSpinner.setAdapter(null);
        sortingList.clear();
        reviewsList.clear();
        publishedReviewsListView.setAdapter(null);
        notPublishedReviewsListView.setAdapter(null);
        clearChoices();
        //sorting options

        sortingList.add("Date");
        sortingList.add("Food"); //ruoka;pvm;restaurant;score
        sortingList.add("Restaurant");
        sortingList.add("Average Score");

        //arrayadapter for spinner
        ArrayAdapter<String> ap = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sortingList);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(ap);

        ArrayAdapter<University> ap4 = new ArrayAdapter<University>(getContext(), android.R.layout.simple_list_item_1, parseClass.getUniversities());
        ap4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(ap4);
        universitySpinner.setSelection(user.getHomeUniversityPos());

        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Reads restaurant XML file again based on the selected university
                parseClass.getRestaurants().clear();
                clearChoices();
                parseClass.parseRestaurantsMenu(position, context);
                ArrayAdapter<Restaurant> arrayAdapter = new ArrayAdapter<Restaurant>(getActivity(), android.R.layout.simple_spinner_item, parseClass.getRestaurants());
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                restaurantSpinner.setAdapter(arrayAdapter);

                restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        clearChoices();
                        selectedRestaurantName = restaurantSpinner.getSelectedItem().toString();
                        parseClass.parseRestaurantReviews(selectedRestaurantName, context);

                        ArrayAdapter<FoodReview> arrayAdapterListView = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, parseClass.getReviewsPublished());
                        publishedReviewsListView.setAdapter(arrayAdapterListView);

                        ArrayAdapter<FoodReview> arrayAdapterListView2 = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, parseClass.getReviewsNotPublished());
                        notPublishedReviewsListView.setAdapter(arrayAdapterListView2);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //arrayadapter for publishedreviews listview
        ArrayAdapter<String> ap2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, reviewsList);
        publishedReviewsListView.setAdapter(ap2);
        notPublishedReviewsListView.setAdapter(ap2);



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

    //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
        publishedReviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                EditReviewsFragment editReviewsFragment = new EditReviewsFragment();

                Bundle bundle = new Bundle();
                FoodReview selectedReview = parseClass.getReviewsPublished().get(i);
                bundle.putSerializable("reviewKey", selectedReview);
                bundle.putSerializable("allReviews", parseClass.getAllReviews());
                editReviewsFragment.setArguments(bundle);
                ft.replace(R.id.fragment_container, editReviewsFragment);
                ft.addToBackStack("edit_own_reviews_fragment");
                ft.commit();

                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editReviewsFragment).addToBackStack("edit_own_reviews_fragment").commit();

            }
        });
        notPublishedReviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                EditReviewsFragment editReviewsFragment = new EditReviewsFragment();

                Bundle bundle = new Bundle();
                FoodReview selectedReview = parseClass.getReviewsNotPublished().get(i);
                bundle.putSerializable("reviewKey", selectedReview);
                editReviewsFragment.setArguments(bundle);
                ft.replace(R.id.fragment_container, editReviewsFragment);
                ft.addToBackStack("edit_own_reviews_fragment");
                ft.commit();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editReviewsFragment).addToBackStack("edit_own_reviews_fragment").commit();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        clearChoices();
        universitySpinner.setSelection(user.getHomeUniversityPos());
    }

    private void clearChoices(){
        parseClass.getReviewsNotPublished().clear();
        parseClass.getReviewsPublished().clear();
        publishedReviewsListView.clearChoices();
        notPublishedReviewsListView.clearChoices();
    }

}
