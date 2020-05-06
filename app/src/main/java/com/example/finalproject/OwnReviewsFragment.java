package com.example.finalproject;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnReviewsFragment extends Fragment {

    private Spinner sortingSpinner;
    private Spinner universitySpinner;
    private Spinner restaurantSpinner;
    private List<String> sortingList = new ArrayList<String>();
    private List<String> reviewsList = new ArrayList<String>();
    private ListView notPublishedReviewsListView ;
    private ListView publishedReviewsListView ;
    private String selectedRestaurantName;
    private View v;
    private EditReviewsFragment editReviewsFragment = new EditReviewsFragment();
    private User user = User.getInstance();
    private ParseClass parseClass = ParseClass.getInstance();
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


        //clear spinner and list views
        sortingSpinner.setAdapter(null);
        sortingList.clear();
        reviewsList.clear();
        publishedReviewsListView.setAdapter(null);
        notPublishedReviewsListView.setAdapter(null);
        clearChoices();

        //sorting options
        sortingList.add(getResources().getString(R.string.ownReviewsView_date));
        sortingList.add(getResources().getString(R.string.ownReviewsView_food));
        sortingList.add(getResources().getString(R.string.ownReviewsView_overallScore));
        sortingList.add(getResources().getString(R.string.ownReviewsView_vote));

        //ArrayAdapter for sortingSpinner
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sortingList);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(sortAdapter);

        //ArrayAdapter for universitySpinner
        ArrayAdapter<University> uniAdapter = new ArrayAdapter<University>(getContext(), android.R.layout.simple_list_item_1, parseClass.getUniversities());
        uniAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(uniAdapter);

        //Sets home university as default selection
        universitySpinner.setSelection(user.getHomeUniversityPos());

        //University spinner listener
        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //clears restaurants and list views
                parseClass.getRestaurants().clear();
                clearChoices();

                //Reads restaurant XML file again based on the selected university and fills restaurants ArrayList in parseClass
                parseClass.parseRestaurantsMenu(position, context);

                //arrayAdapter for Restaurants
                ArrayAdapter<Restaurant> resAdapter = new ArrayAdapter<Restaurant>(getActivity(), android.R.layout.simple_spinner_item, parseClass.getRestaurants());
                resAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                restaurantSpinner.setAdapter(resAdapter);

                //Restaurant spinner listener
                restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //clears list views
                        clearChoices();

                        selectedRestaurantName = restaurantSpinner.getSelectedItem().toString();

                        //reads reviews from XML and fills published, not published and all reviews.
                        parseClass.parseRestaurantReviews(selectedRestaurantName, context);

                        //ArrayAdapter for published reviews list view
                        ArrayAdapter<FoodReview> publishedAdapter = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, parseClass.getReviewsPublished());
                        publishedReviewsListView.setAdapter(publishedAdapter);

                        //ArrayAdapter for not published reviews list view
                        ArrayAdapter<FoodReview> notPublishedAdapter = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, parseClass.getReviewsNotPublished());
                        notPublishedReviewsListView.setAdapter(notPublishedAdapter);
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


        //sorting spinner listener
        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
               switch (position) {
                   case 0:
                        //https://www.youtube.com/watch?v=Mguw_TQBExo how to use Collections.sort
                       //sort by date using Collections.sort
                       Sorting.sortByDate(parseClass.getReviewsPublished());

                       //Reverses the list
                       Collections.reverse(parseClass.getReviewsPublished());
                       //setting new adapter for published reviews
                       ArrayAdapter<FoodReview> arrayAdapterDatePublish = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1,  parseClass.getReviewsPublished());
                       publishedReviewsListView.setAdapter(arrayAdapterDatePublish);

                       //sort by date using Collections.sort
                       Sorting.sortByDate(parseClass.getReviewsNotPublished());
                       //Reverses the list
                       Collections.reverse(parseClass.getReviewsNotPublished());
                       //setting new adapter for not published reviews
                       ArrayAdapter<FoodReview> arrayAdapterDateNotPublished = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, parseClass.getReviewsNotPublished());
                       notPublishedReviewsListView.setAdapter(arrayAdapterDateNotPublished);
                       break;

                   case 1:
                       //https://www.youtube.com/watch?v=Mguw_TQBExo how to use Collections.sort
                       //sort by food using Collections.sort
                       Sorting.sortByFood(parseClass.getReviewsPublished());

                       //setting new adapter for published reviews
                       ArrayAdapter<FoodReview> arrayAdapterFoodPublished = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1,  parseClass.getReviewsPublished());
                       publishedReviewsListView.setAdapter(arrayAdapterFoodPublished);

                       //sort by date using Collections.sort
                       Sorting.sortByFood(parseClass.getReviewsNotPublished());

                       //setting new adapter for not published reviews
                       ArrayAdapter<FoodReview> arrayAdapterFoodNotPublished = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, parseClass.getReviewsNotPublished());
                       notPublishedReviewsListView.setAdapter(arrayAdapterFoodNotPublished);
                       break;

                   case 2:
                       //https://stackoverflow.com/questions/9941890/sorting-arraylist-of-objects-by-float how to sort by float
                       //sort by overall score using Collections.sort
                       Sorting.sortByScore(parseClass.getReviewsPublished());

                       //reverse the list
                       Collections.reverse(parseClass.getReviewsPublished());

                       //setting new adapter for published reviews
                       ArrayAdapter<FoodReview> arrayAdapterScorePublished = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1,  parseClass.getReviewsPublished());
                       publishedReviewsListView.setAdapter(arrayAdapterScorePublished);

                       //sort by date using Collections.sort
                       Sorting.sortByScore(parseClass.getReviewsNotPublished());

                       //reverse the list
                       Collections.reverse(parseClass.getReviewsNotPublished());

                       //setting new adapter for not published reviews
                       ArrayAdapter<FoodReview> arrayAdapterScoreNotPublished = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, parseClass.getReviewsNotPublished());
                       notPublishedReviewsListView.setAdapter(arrayAdapterScoreNotPublished);
                       break;
                   case 3:

                       Sorting.sortByVote(parseClass.getReviewsPublished());

                       Collections.reverse(parseClass.getReviewsPublished());

                       ArrayAdapter<FoodReview> arrayAdapterVote = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1,  parseClass.getReviewsPublished());
                       publishedReviewsListView.setAdapter(arrayAdapterVote);


                       Sorting.sortByVote(parseClass.getReviewsNotPublished());

                       Collections.reverse(parseClass.getReviewsNotPublished());

                       ArrayAdapter<FoodReview> arrayAdapterVoteNotPublished = new ArrayAdapter<FoodReview>(getActivity(), android.R.layout.simple_list_item_1, parseClass.getReviewsNotPublished());
                       notPublishedReviewsListView.setAdapter(arrayAdapterVoteNotPublished);


                   default:
                       break;
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //https://stackoverflow.com/questions/42266436/passing-objects-between-fragments
        //When clicked on a published review, passes data to published review editing fragment and moves to new fragment (editReviewsFragment)
        //sends selected review and it's information
        publishedReviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                EditReviewsFragment editReviewsFragment = new EditReviewsFragment();

                Bundle bundle = new Bundle();
                FoodReview selectedReview = parseClass.getReviewsPublished().get(i);
                bundle.putSerializable("reviewKey", selectedReview);
                editReviewsFragment.setArguments(bundle);
                ft.replace(R.id.fragment_container, editReviewsFragment);
                ft.commit();
            }
        });

        //When clicked on a not published review, passes data to not published review editing fragment and moves to new fragment (editReviewsFragment)
        //sends selected review and it's information
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
                ft.commit();
            }
        });
        return v;
    }


    //On resume clears list views and sets user's home university and universitySpinner selection
    @Override
    public void onResume() {
        super.onResume();
        clearChoices();
        universitySpinner.setSelection(user.getHomeUniversityPos());
    }

    //clears published and not published list views.
    private void clearChoices(){
        parseClass.getReviewsNotPublished().clear();
        parseClass.getReviewsPublished().clear();
        publishedReviewsListView.clearChoices();
        notPublishedReviewsListView.clearChoices();
    }

}