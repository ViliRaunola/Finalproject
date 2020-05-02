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
    private ListView notPublishedReviews;
    private ListView publishedReviews;
    private String selectedSortingMethod;
    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private ArrayList<University> universities;
    private ArrayList<FoodReview> reviews = new ArrayList<FoodReview>();
    private University selectedUniversity;
    private int restaurantPosition;
    private String selectedRestaurantName;
    View v;
    EditReviewsFragment editReviewsFragment = new EditReviewsFragment();
    User user = User.getInstance();
    private Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_own_reviews, container, false);

        context = this.getContext();

        publishedReviews = (ListView) v.findViewById(R.id.listViewPublishedReviews);
        notPublishedReviews = (ListView) v.findViewById(R.id.listViewNotPublishedReviews);
        sortingSpinner = (Spinner) v.findViewById(R.id.ownReviews_sorting_spinner);
        universitySpinner = (Spinner)v.findViewById(R.id.universitySpinner_ownReviews);
        restaurantSpinner = (Spinner)v.findViewById(R.id.restaurantSpinner_ownReviews);

        try {
            universities = (ArrayList<University>) getArguments().getSerializable("key2");
        }catch (Exception e){
            e.printStackTrace();
        }


        //clear spinner and list
        sortingSpinner.setAdapter(null);
        sortingList.clear();
        reviewsList.clear();
        publishedReviews.setAdapter(null);
        notPublishedReviews.setAdapter(null);
        //sorting options

        sortingList.add("Date");
        sortingList.add("Food"); //ruoka;pvm;restaurant;score
        sortingList.add("Restaurant");
        sortingList.add("Average Score");

        //arrayadapter for spinner
        ArrayAdapter<String> ap = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sortingList);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(ap);

        ArrayAdapter<University> ap4 = new ArrayAdapter<University>(getContext(), android.R.layout.simple_list_item_1, universities);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(ap4);
        universitySpinner.setSelection(user.getHomeUniversityPos());

        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Reads restaurant XML file again based on the selected university
                restaurants.clear();
                selectedUniversity = universities.get(position);
                parseRestaurantsMenu(position);
                ArrayAdapter<Restaurant> arrayAdapter = new ArrayAdapter<Restaurant>(getActivity(), android.R.layout.simple_spinner_item, restaurants);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                restaurantSpinner.setAdapter(arrayAdapter);

                restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedRestaurantName = restaurantSpinner.getSelectedItem().toString();
                        parseRestaurantReviews(selectedRestaurantName);
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

        //test list
        reviewsList.add("Food:  Lihapullat ja perunamuusi\nDate:  28.4.2020\nRestaurant:  Laseri\nAverage Score:  4.5 stars");
        reviewsList.add("Food:  Lohipullat ja perunamuusi\nDate:  29.4.2020\nRestaurant:  Lut Buffet\nAverage Score:  4.0 stars");
        reviewsList.add("Food:  Lohipullat ja perunamuusi\nDate:  29.4.2020\nRestaurant:  Lut Buffet\nAverage Score:  4.0 stars");



        //arrayadapter for publishedreviews listview
        ArrayAdapter<String> ap2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, reviewsList);
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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editReviewsFragment).addToBackStack("edit_own_reviews_fragment").commit();

            }
        });
        notPublishedReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editReviewsFragment).addToBackStack("edit_own_reviews_fragment").commit();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        universitySpinner.setSelection(user.getHomeUniversityPos());
    }



    public void parseRestaurantsMenu(int pos) {
        University selectedUniversity = universities.get(pos);
        for (String s : selectedUniversity.restaurantsXML) {
            try (InputStream ins = getContext().getAssets().open(s)) {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document xmlDoc = documentBuilder.parse(ins);
                NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("restaurant");
                System.out.println(nodeList.getLength());

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
                System.out.println("###########" + universities.size() + "############");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseRestaurantReviews(String selectedRestaurantName){

        try (FileInputStream ins = new FileInputStream (new File(context.getFilesDir() +"/reviews/" + selectedRestaurantName + "_Reviews.xml"))){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("review");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String foodId = element.getElementsByTagName("foodId").item(0).getTextContent();

                    String userId = element.getElementsByTagName("userId").item(0).getTextContent();

                    String tasteScore = element.getElementsByTagName("tasteScore").item(0).getTextContent();
                    float tasteScoreFloat = Float.parseFloat(tasteScore);

                    String lookScore = element.getElementsByTagName("lookScore").item(0).getTextContent();
                    float lookScoreFloat = Float.parseFloat(lookScore);

                    String textureScore = element.getElementsByTagName("textureScore").item(0).getTextContent();
                    float textureScoreFloat = Float.parseFloat(textureScore);

                    String reviewText = element.getElementsByTagName("reviewText").item(0).getTextContent();

                    String reviewDate = element.getElementsByTagName("date").item(0).getTextContent();
                    Date reviewDateDate = simpleDateFormat.parse(reviewDate);

                    FoodReview review = new FoodReview(foodId, reviewDateDate, tasteScoreFloat, lookScoreFloat, textureScoreFloat, reviewText, userId);
                    reviews.add(review);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



}
