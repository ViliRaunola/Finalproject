package com.example.finalproject;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UniversityFragment extends Fragment implements Serializable {

    private ListView foodItemLisView;
    private TextView infoWin;
    private TextView dayTextView;
    private Spinner restaurantSpinner;
    private Spinner universitySpinner;
    private Button previousDayButton;
    private Button nextDayButton;
    private Button currentDayButton;
    private int toDayInt;
    private ArrayList<FoodItem> dailyFoods = new ArrayList<FoodItem>();
    private int restaurantPosition;
    User user = User.getInstance();
    Context context;
    ParseClass parseClass = ParseClass.getInstance();
    DateClass dateClass = DateClass.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.university, container, false);
        dayTextView = (TextView)v.findViewById(R.id.dayTextView);
        infoWin = (TextView)v.findViewById(R.id.infoWindow);
        universitySpinner = (Spinner)v.findViewById(R.id.university_spinner);
        foodItemLisView = (ListView) v.findViewById(R.id.listViewFood);
        restaurantSpinner = (Spinner)v.findViewById(R.id.restaurant_spinner);
        previousDayButton = v.findViewById(R.id.previousDayButton);
        nextDayButton = v.findViewById(R.id.nextDayButton);
        currentDayButton = v.findViewById(R.id.currentDayButton);

        context = getContext();

        /*
        Populating the first spinner on the page.
        Calls parse class to read all universities from "database" aka phones own memory.
         */
        toDayInt = dateClass.getTodayInt();
        parseClass.getUniversities().clear();
        parseClass.parseUniversity(context);
        ArrayAdapter<University> ap = new ArrayAdapter<University>(getActivity(), android.R.layout.simple_list_item_1, parseClass.getUniversities());
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(ap);
        universitySpinner.setSelection(user.getHomeUniversityPos());
        ap.notifyDataSetChanged();


        //On select adds specific restaurants to restaurantSpinner depending the selected university.
        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayTextView.setText(dateClass.getCurrentDateWithWeekDay());
                toDayInt = dateClass.getTodayInt();
                parseClass.getRestaurants().clear();

                parseClass.parseRestaurantsMenu(position, context);
                University selectedUniversity = parseClass.getUniversities().get(position);
                infoWin.setText(selectedUniversity.getInfo());

                ArrayAdapter<Restaurant> arrayAdapter = new ArrayAdapter<Restaurant>(getActivity(), android.R.layout.simple_spinner_item,  parseClass.getRestaurants());
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                restaurantSpinner.setAdapter(arrayAdapter);

                /*
                Food items come from "unisRestaurantAndMenus"-file which's address is searched from
                corresponding university's xml file that contains that university's all restaurants.
                 */
                restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        restaurantPosition = position;
                        parseClass.getRestaurants().get(restaurantPosition).resDailyMenu.clear();

                        parseClass.parseFoodItems(restaurantPosition, context);

                        checkCurrentDay(toDayInt, restaurantPosition);

                        ArrayAdapter<FoodItem> arrayAdapterListView = new ArrayAdapter<FoodItem>(getActivity(), android.R.layout.simple_list_item_1, dailyFoods);
                        foodItemLisView.setAdapter(arrayAdapterListView);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Gets today's restaurant menu and resets date
        currentDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayTextView.setText(dateClass.getCurrentDateWithWeekDay());
                toDayInt = dateClass.getTodayInt();
                checkCurrentDay(toDayInt, restaurantPosition);
                foodItemLisView.invalidateViews();
            }
        });

        /*
        These on click listeners for previous and next day are for displaying right day on the text field.
        Also disables the button if user gets to the last day of the menu.
         */
        previousDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDayButton.setEnabled(true);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE\ndd.MM.yyyy");
                try {
                    if(toDayInt <= 1){
                        Toast.makeText(getContext(),"End of monthly menu",Toast.LENGTH_SHORT).show();
                        previousDayButton.setEnabled(false);
                    }else {
                        Date selectedDate = simpleDateFormat.parse(dayTextView.getText().toString());
                        String previousDate = simpleDateFormat.format(dateClass.changeDate(selectedDate, -1));
                        dayTextView.setText(previousDate);
                        toDayInt -= 1;
                        checkCurrentDay(toDayInt, restaurantPosition);
                        foodItemLisView.invalidateViews();
                    }
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });
        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousDayButton.setEnabled(true);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE\ndd.MM.yyyy");
                try {
                    if(toDayInt >=  parseClass.getRestaurants().get(0).resDailyMenu.get( parseClass.getRestaurants().get(0).resDailyMenu.size()-1).getDay()) {
                        Toast.makeText(getContext(),"End of monthly menu",Toast.LENGTH_SHORT).show();
                        nextDayButton.setEnabled(false);
                    }else{
                        Date selectedDate = simpleDateFormat.parse(dayTextView.getText().toString());
                        String nextDate = simpleDateFormat.format(dateClass.changeDate(selectedDate, 1));
                        dayTextView.setText(nextDate);
                        toDayInt += 1;
                        checkCurrentDay(toDayInt, restaurantPosition);
                        foodItemLisView.invalidateViews();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        /*
        When user taps on selected food item a new fragment called FoodReviewsFragment is opened.
        This sends selected food object, selected restaurant name and the day of the
         */
        foodItemLisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                FoodReviewsFragment foodReviewsFragment = new FoodReviewsFragment();

                Bundle bundle = new Bundle();
                FoodItem selectedFood = dailyFoods.get(i);
                String selectedRestaurantName = restaurantSpinner.getSelectedItem().toString();
                String dayOfFoodString = dayTextView.getText().toString();
                bundle.putSerializable("FoodKey", selectedFood);
                bundle.putString("resKey", selectedRestaurantName);
                bundle.putString("dateKey", dayOfFoodString);
                foodReviewsFragment.setArguments(bundle);

                ft.replace(R.id.fragment_container, foodReviewsFragment);
                ft.addToBackStack("food_reviews_fragment");
                ft.commit();
            }
        });
        return v;
    }

    //Goes through restaurants resDailyMenu list to find correct food responding to the wanted date.
    public void checkCurrentDay(int day, int restaurantPlace){
        dailyFoods.clear();
        for(int i = 0; i <  parseClass.getRestaurants().get(restaurantPlace).resDailyMenu.size(); i++){
            FoodItem foodItem =  parseClass.getRestaurants().get(restaurantPlace).resDailyMenu.get(i);
            if(foodItem.getDay() == day){
                dailyFoods.add(foodItem);
            }
        }
    }

    /*
    When reloading this fragment
    users home university is set as the first university on the spinner.
     */
    @Override
    public void onResume() {
        super.onResume();
        universitySpinner.setSelection(user.getHomeUniversityPos());
    }

}
