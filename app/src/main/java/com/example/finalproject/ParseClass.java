package com.example.finalproject;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParseClass extends AppCompatActivity {

    private ArrayList<University> universities = new ArrayList<University>();
    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private ArrayList<FoodReview> allReviews = new ArrayList<FoodReview>();
    private ArrayList<FoodReview> reviewsPublished = new ArrayList<FoodReview>();
    private ArrayList<FoodReview> reviewsNotPublished = new ArrayList<FoodReview>();

    //Initialize singleton class
    private static ParseClass parseClass = new ParseClass();
    private ParseClass() {
    }
    public static ParseClass getInstance(){
        return parseClass;
    }


    //Get methods
    public ArrayList<University> getUniversities(){
        return this.universities;
    }

    public ArrayList<Restaurant> getRestaurants(){
        return this.restaurants;
    }

    public ArrayList<FoodReview> getAllReviews(){
        return this.allReviews;
    }

    public ArrayList<FoodReview> getReviewsPublished(){
        return this.reviewsPublished;
    }

    public ArrayList<FoodReview> getReviewsNotPublished(){
        return this.reviewsNotPublished;
    }


    //Parses "university.xml" and creates University objects based on .xml parameters. Adds these new University objects to "universities"-ArrayList.
    //This ArrayList is shown in university_spinner.
    public void parseUniversity(Context context){

        try (InputStream ins = context.getAssets().open("university.xml")){
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
    public void parseRestaurantsMenu(int pos, Context context) {
        University selectedUniversity = parseClass.universities.get(pos);
        //university info to textView

        for (String s : selectedUniversity.restaurantsXML) {
            try (InputStream ins = context.getAssets().open(s)) {
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
    public void parseFoodItems(int position, Context context) {
        Restaurant selectedRestaurant =  parseClass.getRestaurants().get(position);
        for (String s : selectedRestaurant.restaurantMenusXML) {
            try (InputStream ins = context.getAssets().open(s)) {
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


    //Gets selected restaurant reviews from the user.
    public void parseRestaurantReviews(String selectedRestaurantName, Context context){
        User user = User.getInstance();
        try (FileInputStream ins = new FileInputStream (new File(context.getFilesDir() +"/reviews/" + selectedRestaurantName + "_Reviews.xml"))){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("review");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            reviewsPublished.clear();
            reviewsNotPublished.clear();

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String reviewId = element.getElementsByTagName("reviewId").item(0).getTextContent();

                    String foodId = element.getElementsByTagName("foodId").item(0).getTextContent();

                    String foodName = element.getElementsByTagName("foodName").item(0).getTextContent();

                    String userId = element.getElementsByTagName("userId").item(0).getTextContent();

                    String published = element.getElementsByTagName("published").item(0).getTextContent();
                    Boolean publishedBoolean = Boolean.parseBoolean(published);

                    String tasteScore = element.getElementsByTagName("tasteScore").item(0).getTextContent();
                    float tasteScoreFloat = Float.parseFloat(tasteScore);

                    String lookScore = element.getElementsByTagName("lookScore").item(0).getTextContent();
                    float lookScoreFloat = Float.parseFloat(lookScore);

                    String textureScore = element.getElementsByTagName("textureScore").item(0).getTextContent();
                    float textureScoreFloat = Float.parseFloat(textureScore);

                    String reviewText = element.getElementsByTagName("reviewText").item(0).getTextContent();

                    String reviewDate = element.getElementsByTagName("date").item(0).getTextContent();
                    Date reviewDateDate = simpleDateFormat.parse(reviewDate);

                    FoodReview review = new FoodReview(reviewId, publishedBoolean,foodId, foodName, selectedRestaurantName, reviewDateDate, tasteScoreFloat, lookScoreFloat, textureScoreFloat, reviewText, userId);
                    allReviews.add(review);
                    if(Integer.parseInt(userId) == user.getUserID()){
                        if(publishedBoolean){
                            reviewsPublished.add(review);
                        }else{
                            reviewsNotPublished.add(review);
                        }
                    }
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
