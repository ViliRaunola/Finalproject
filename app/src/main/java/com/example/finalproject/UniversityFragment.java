package com.example.finalproject;


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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class UniversityFragment extends Fragment implements Serializable {

    private ListView foodItemLisView;
    public ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private TextView infoWin;
    public ArrayList<University> universities = new ArrayList<University>();
    private TextView dayTextView;
    private Spinner restaurantSpinner;
    private Spinner universitySpinner;
    private Button previousDayButton;
    private Button nextDayButton;
    private ArrayList<String> weekDays = new ArrayList<String>();
    EditReviewsFragment editReviewsFragment = new EditReviewsFragment();
    //These are for showing the right food item in the day
    private int toDayInt;
    private ArrayList<FoodItem> dailyFoods = new ArrayList<FoodItem>();
    private int restaurantPostion;
    private int foodMenuMaxLenght = 1;
    User user = User.getInstance();

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

        getToDayInt();
        universities.clear();
        parseUniversity();
        ArrayAdapter<University> ap = new ArrayAdapter<University>(getActivity(), android.R.layout.simple_list_item_1, universities);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(ap);
        System.out.println(savedInstanceState);
        universitySpinner.setSelection(user.getHomeUniversityPos());
        ap.notifyDataSetChanged();

        weekDays.clear();
        weekDays.add("Monday");
        weekDays.add("Tuesday");
        weekDays.add("Wednesday");
        weekDays.add("Thursday");
        weekDays.add("Friday");
        weekDays.add("Saturday");
        weekDays.add("Sunday");
        dayTextView.setText(getCurrentDate());



        //On select adds specific restaurants to restaurantSpinner depending the selected university
        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getToDayInt();
                restaurants.clear();

                parseRestaurantsMenu(position);

                ArrayAdapter<Restaurant> arrayAdapter = new ArrayAdapter<Restaurant>(getActivity(), android.R.layout.simple_spinner_item, restaurants);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                restaurantSpinner.setAdapter(arrayAdapter);
                //Here is listView + array adapter for it. This listView is meant for food items.
                //Food items come from some xml file; not sure yet from where??!
                restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        restaurantPostion = position;
                        restaurants.get(restaurantPostion).resDailyMenu.clear();
                        Restaurant selectedRestaurant = restaurants.get(position);
                        parseFoodItems(selectedRestaurant);

                        checkCurrentDay(toDayInt, restaurantPostion);

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



        //These on click listeners are for displaying right day on the text field.
        previousDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(toDayInt <= 1){
                    Toast.makeText(getContext(), "No more", Toast.LENGTH_SHORT).show();
                }else {
                    toDayInt -= 1;
                    if (dayTextView.getText().equals("Monday")) {
                        dayTextView.setText(weekDays.get(6));
                    } else if (dayTextView.getText().equals("Tuesday")) {
                        dayTextView.setText(weekDays.get(0));
                    } else if (dayTextView.getText().equals("Wednesday")) {
                        dayTextView.setText(weekDays.get(1));
                    } else if (dayTextView.getText().equals("Thursday")) {
                        dayTextView.setText(weekDays.get(2));
                    } else if (dayTextView.getText().equals("Friday")) {
                        dayTextView.setText(weekDays.get(3));
                    } else if (dayTextView.getText().equals("Saturday")) {
                        dayTextView.setText(weekDays.get(4));
                    } else if (dayTextView.getText().equals("Sunday")) {
                        dayTextView.setText(weekDays.get(5));
                    }
                    checkCurrentDay(toDayInt, restaurantPostion);
                    foodItemLisView.invalidateViews();
                }
            }
        });
        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toDayInt >= restaurants.get(0).resDailyMenu.get(restaurants.get(0).resDailyMenu.size()-1).getDay()) {
                    System.out.println("###########################   "+toDayInt +"  ###########################   ");
                    System.out.println("###########################   "+restaurants.get(0).resDailyMenu.get(restaurants.get(0).resDailyMenu.size()-1).getDay() +"  ###########################   ");
                    Toast.makeText(getContext(), "No more", Toast.LENGTH_SHORT).show();
                }else{
                    if (dayTextView.getText().equals("Monday")) {
                        dayTextView.setText(weekDays.get(1));
                    } else if (dayTextView.getText().equals("Tuesday")) {
                        dayTextView.setText(weekDays.get(2));
                    } else if (dayTextView.getText().equals("Wednesday")) {
                        dayTextView.setText(weekDays.get(3));
                    } else if (dayTextView.getText().equals("Thursday")) {
                        dayTextView.setText(weekDays.get(4));
                    } else if (dayTextView.getText().equals("Friday")) {
                        dayTextView.setText(weekDays.get(5));
                    } else if (dayTextView.getText().equals("Saturday")) {
                        dayTextView.setText(weekDays.get(6));
                    } else if (dayTextView.getText().equals("Sunday")) {
                        dayTextView.setText(weekDays.get(0));
                    }
                    toDayInt += 1;
                    checkCurrentDay(toDayInt, restaurantPostion);
                    foodItemLisView.invalidateViews();
                }
            }
        });

        foodItemLisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editReviewsFragment).commit();
            }
        });
        return v;
    }


    //Goes through restaurants resDailyMenu list to find correct food responding to the wanted date.
    public void checkCurrentDay(int day, int restaurantPlace){
        foodMenuMaxLenght = 0;
        dailyFoods.clear();
        for(int i = 0; i < restaurants.get(restaurantPlace).resDailyMenu.size(); i++){
            FoodItem foodItem = restaurants.get(restaurantPlace).resDailyMenu.get(i);
            if(foodItem.getDay() == day){
                dailyFoods.add(foodItem);
            }else{
                foodMenuMaxLenght += 1;
                continue;
            }
        }
    }



    public void getToDayInt(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String temp = dtf.format(now);
        String time[] = temp.split(" "); //time[0] == yyyy/MM/dd
        final String day[] = time[0].split("/"); //day[2] == d
        toDayInt = Integer.parseInt(day[2]);
    }


    //Parses "university.xml" and creates University objects based on .xml parameters. Adds these new University objects to "universities"-ArrayList.
    //This ArrayList is shown in university_spinner.
    public void parseUniversity(){

        try (InputStream ins = getContext().getAssets().open("university.xml")){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("university");

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String uniName = element.getElementsByTagName("universityName").item(0).getTextContent();
                    String uniId = element.getElementsByTagName("id").item(0).getTextContent();
                    String resXMLFileName = element.getElementsByTagName("restaurantXml").item(0).getTextContent();
                    String uniInfoText = element.getElementsByTagName("restaurantInfo").item(0).getTextContent();
                    University university = new University(uniName, uniId, uniInfoText);
                    university.addToRestaurants(resXMLFileName);
                    universities.add(university);

                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    //Parses a specific XML file depending on the selected university from the university spinner
    public void parseRestaurantsMenu(int pos) {
            University selectedUniversity = universities.get(pos);
            //university info to textView
            infoWin.setText(selectedUniversity.getInfo());
            for (String s : selectedUniversity.restaurantsXML) {
                try (InputStream ins = getContext().getAssets().open(s)) {
                    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document xmlDoc = documentBuilder.parse(ins);
                    NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("restaurant");

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            String resName = element.getElementsByTagName("restaurantName").item(0).getTextContent();
                            String resMenuName = element.getElementsByTagName("restaurantMenu").item(0).getTextContent();
                            Restaurant restaurant = new Restaurant(resName);
                            restaurant.addToRestaurantMenusXML(resMenuName);
                            restaurants.add(restaurant);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
    }
    //parses food items from XML files of the chosen Restaurant. Adds them to Restaurant's dailyMenus.
    public void parseFoodItems(Restaurant selectedRestaurant) {
        for (String s : selectedRestaurant.restaurantMenusXML) {
            try (InputStream ins = getContext().getAssets().open(s)) {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document xmlDoc = documentBuilder.parse(ins);
                NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("food");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String foodName = element.getElementsByTagName("name").item(0).getTextContent();
                        String foodPrice = element.getElementsByTagName("price").item(0).getTextContent();
                        String foodId = element.getElementsByTagName("id").item(0).getTextContent();
                        String foodDay = element.getElementsByTagName("day").item(0).getTextContent();
                        int foodDayInt = Integer.parseInt(foodDay);
                        FoodItem foodItem = new FoodItem(foodName, foodPrice, foodId, foodDayInt);
                        selectedRestaurant.addFoodItemToDailyMenu(foodItem);

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
    }


    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }


    @Override
    public void onResume() {
        super.onResume();
        universitySpinner.setSelection(user.getHomeUniversityPos());
    }
}
