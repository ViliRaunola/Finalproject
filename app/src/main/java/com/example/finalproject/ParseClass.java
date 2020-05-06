package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/*
This class handles almost all the file reads and writes.
Does it for both file types; xml and json.
Stores also information (makes objects based on values read from files) to ArrayLists.
 */
public class ParseClass extends AppCompatActivity {
    private ArrayList<University> universities = new ArrayList<University>();
    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private ArrayList<FoodReview> allReviews = new ArrayList<FoodReview>();
    private ArrayList<FoodReview> reviewsPublished = new ArrayList<FoodReview>();
    private ArrayList<FoodReview> reviewsNotPublished = new ArrayList<FoodReview>();
    private int biggestReviewId;

    //Initializing singleton class.
    private static ParseClass parseClass = new ParseClass();
    private ParseClass() {
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
    public static ParseClass getInstance(){
        return parseClass;
    }
    public int getBiggestReviewId() {
        return this.biggestReviewId;
    }

    //Parses "university.xml" and creates University objects based on .xml parameters. Adds these new University objects to "universities"-ArrayList.
    //This ArrayList is shown in university_spinner.
    public void parseUniversity(Context context){

        try (FileInputStream fis = new FileInputStream (new File(context.getFilesDir() +"/unisRestaurantsAndMenus/university.xml"))){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(fis);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("university");
            fis.close();
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



    /*
    Parses a specific XML file depending on the selected university position that is the same as the university spinner.
    The position of the selected university is same in the spinner and universities list because same list is used to
    populate the spinner.
     */
    public void parseRestaurantsMenu(int pos, Context context) {
        University selectedUniversity = parseClass.universities.get(pos);
        //University info to textView
        for (String s : selectedUniversity.restaurantsXML) {
            try (FileInputStream fis = new FileInputStream (new File(context.getFilesDir() +"/unisRestaurantsAndMenus/"+ s))) {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document xmlDoc = documentBuilder.parse(fis);
                NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("restaurant");
                fis.close();
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

    //Parses food items from XML files of the chosen Restaurant. Makes them into objects and adds them to Restaurant's dailyMenus.
    public void parseFoodItems(int position, Context context) {
        Restaurant selectedRestaurant =  parseClass.getRestaurants().get(position);
        String language = Locale.getDefault().getLanguage();
        for (String s : selectedRestaurant.restaurantMenusXML) {
            try (FileInputStream fis = new FileInputStream (new File(context.getFilesDir() +"/unisRestaurantsAndMenus/"+ language + "_" + s))) {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document xmlDoc = documentBuilder.parse(fis);
                NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("food");
                fis.close();
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




    //Gets selected restaurant reviews from the user and makes it into object.
    //Populates ArrayLists: allReviews, reviewsPublished and reviewsNotPublished.
    public void parseRestaurantReviews(String selectedRestaurantName, Context context){
        biggestReviewId = 0; //This is for keeping track of the biggest review Id so that when creating a new review we can give it a id that isn't already in use.
        User user = User.getInstance();
        try (FileInputStream fis = new FileInputStream (new File(context.getFilesDir() +"/reviews/" + selectedRestaurantName + "_Reviews.xml"))){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(fis);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("review");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            fis.close();
            //Making sure that ArrayLists are empty before rewriting to them.
            reviewsPublished.clear();
            reviewsNotPublished.clear();
            allReviews.clear();

            /*After reading file into node list, method goes through nodeList's items and gets element
            tag's name into variable. Later that variable is used in FoodReviews constructor.
             */
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

                    String voteScoreString = element.getElementsByTagName("voteScore").item(0).getTextContent();
                    int voteScore = Integer.parseInt(voteScoreString);


                    FoodReview review = new FoodReview(reviewId, publishedBoolean,foodId, foodName, selectedRestaurantName, reviewDateDate, tasteScoreFloat, lookScoreFloat, textureScoreFloat, reviewText, userId, voteScore);

                    //settings string for displaying reviews in the correct language
                    review.setAverageScoreString(context.getResources().getString(R.string.ownReviewsView_averageScore));
                    review.setDateString(context.getResources().getString(R.string.ownReviewsView_date));
                    review.setRestaurantString(context.getResources().getString(R.string.ownReviewsView_restaurant));
                    review.setVoteScoreString(context.getResources().getString(R.string.ownReviewsView_voteScore));
                    review.setFoodString(context.getResources().getString(R.string.ownReviewsView_food));

                    allReviews.add(review);

                    //Comparing current review id to our so far biggest id. If it is bigger than ours
                    //we replace our id with it.
                    if(Integer.parseInt(review.getReviewId()) >= biggestReviewId){
                        biggestReviewId = Integer.parseInt(review.getReviewId()) + 1;
                    }

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

    /*
    This method rewrites the restaurants reviews file. It has allReviews list which it goes through.
    It writes the review back to the file if its id doesn't match FoodReviews id that the method is deleting.
    File that is being rewritten is xml type.
     */
    public void removeReviewFromXml(Context context, FoodReview selectedOwnReview) {
        OutputStreamWriter osw = null;
        String s;
        try {
            File file = new File(context.getFilesDir() +"/reviews/" + selectedOwnReview.getRestaurant() + "_Reviews.xml");
            FileOutputStream fos = new FileOutputStream(file);
            s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<reviews>";
            fos.write(s.getBytes());

            for (FoodReview review : allReviews) {
                if (review.getReviewId().equals(selectedOwnReview.getReviewId())){
                    s = "";
                }else {
                    s = "\n   <review>\n" +
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
                            "        <voteScore>" + review.getVoteScore() + "</voteScore>\n" +
                            "    </review>";
                }
                fos.write(s.getBytes());
            }
            s = "\n</reviews>";
            fos.write(s.getBytes());
            fos.close();
            allReviews.clear();
        }catch(NullPointerException npe){
            npe.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    https://stackoverflow.com/questions/17022221/openfileoutput-how-to-create-files-outside-the-data-data-path
    Teacher's coding video
    Rewrites the specific restaurant review file with new information for the selected review
    Basically same as removeReviewFromXml but when finding selectedOwnReview from allReviews ArrayList
    it writes that FoodReview object's information to file.
     */
    public void modifyRestaurantReviewXmlFile(Context context, FoodReview selectedOwnReview) {
        OutputStreamWriter osw = null;
        String s;
        try {
            File file = new File(context.getFilesDir() +"/reviews/" + selectedOwnReview.getRestaurant() + "_Reviews.xml");
            FileOutputStream fos = new FileOutputStream(file);

            s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<reviews>";
            fos.write(s.getBytes());

            for (FoodReview review : allReviews) {
                if (review.getReviewId().equals(selectedOwnReview.getReviewId())){
                    s = "\n   <review>\n" +
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
                            "        <voteScore>" + selectedOwnReview.getVoteScore() + "</voteScore>\n" +
                            "   </review>";
                }else {
                    s = "\n   <review>\n" +
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
                            "        <voteScore>" + review.getVoteScore() + "</voteScore>\n" +
                            "   </review>";
                }
                fos.write(s.getBytes());
            }
            s = "\n</reviews>";
            fos.write(s.getBytes());
            fos.close();
            allReviews.clear();
        }catch(NullPointerException npe){
            npe.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    https://www.tutorialspoint.com/how-to-write-create-a-json-file-using-java
    Writes given user object to its own json file.
     */
    public void writeUserJson(Context context, User user) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        //Here basic information is put to the object
        jsonObject.put("password", user.getPassword());
        jsonObject.put("userId", user.getUserID());
        jsonObject.put("firstName", user.getFirstName());
        jsonObject.put("lastName", user.getLastName());
        jsonObject.put("eMail", user.getEmail());
        jsonObject.put("homeUniversity", user.getHomeUniversity());
        jsonObject.put("adminStatus", user.getIsAdminUser());

        /*
        Here method goes through user's down voted and up voted lists and puts its items
        to JSONArray. Then that JSONArray is put into the jsonObject.
         */
        JSONArray downVoteList = new JSONArray();
        if (user.getDownVotedList().size() > 0) {
            for (int i = 0; i < user.getDownVotedList().size(); i++) {
                JSONObject downVoteObject = new JSONObject();
                downVoteObject.put("reviewId", user.getDownVotedList().get(i));
                downVoteList.put(downVoteObject);
            }
        }
        JSONArray upVoteList = new JSONArray();
        if (user.getUpVotedList().size() > 0) {
            for (int d = 0; d < user.getUpVotedList().size(); d++) {
                JSONObject upVoteObject = new JSONObject();
                upVoteObject.put("reviewId", user.getUpVotedList().get(d));
                upVoteList.put(upVoteObject);
            }
        }
        jsonObject.put("downVoted",downVoteList);
        jsonObject.put("upVoted", upVoteList);
        jsonArray.put(jsonObject);

        //Writing jsonArray to file
        try{
            String x = String.format(context.getFilesDir() + "/userData/User" + user.getUserID() + ".json");
            FileWriter fileWriter = new FileWriter(x);
            fileWriter.write(jsonArray.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



/*
Checks from EmailsAndIds file if current email has already been used.
 */
    public boolean checkIfEmailInUse(String email, Context context) throws IOException, JSONException {
        FileInputStream ins = new FileInputStream (new File(context.getFilesDir() +"/userData/EmailsAndIds.json"));
        int size = ins.available();
        final byte[] buffer = new byte[size];
        ins.read(buffer);
        ins.close();

        String json = new String(buffer, "UTF-8");
        JSONArray originalUserData = new JSONArray(json);

        for (int i = 0; i < originalUserData.length(); i++) {
            JSONObject userObject = originalUserData.getJSONObject(i).getJSONObject("user");
            if (email.equals(userObject.getString("eMail"))){
                return true;
            }
        }
        return  false;
    }

    /*
    https://howtodoinjava.com/library/json-simple-read-write-json-examples/
    When new user is created or current user's data has been changed this method is called to update EmailsAndIds json file
    with the current users information.
     */
    public void modifyEmailsAndIds(Context context, User user) throws JSONException, IOException {
        //Reading original file to buffer.
        FileInputStream ins = new FileInputStream (new File(context.getFilesDir() +"/userData/EmailsAndIds.json"));
        int size = ins.available();
        final byte[] buffer = new byte[size];
        ins.read(buffer);
        ins.close();

        //Putting buffer into JSONArray.
        String json = new String(buffer, "UTF-8");
        JSONArray originalUserData = new JSONArray(json);
        JSONArray newUserData = new JSONArray();
        FileWriter fileWriter = new FileWriter((context.getFilesDir() + "/userData/EmailsAndIds.json"));

        //Going through original EmailsAnsIds file.
        for (int i = 0; i < originalUserData.length(); i++) {
            JSONObject userObject = originalUserData.getJSONObject(i).getJSONObject("user");

            //Changing new user email for the same user id.
            if (Integer.parseInt(userObject.getString("userId")) == user.getUserID()) {
                userObject.put("eMail", user.getEmail());
            }

            //Create a newUserObject and adding it to newUserData.
            JSONObject newUserObject = new JSONObject();
            newUserObject.put("user", userObject);
            newUserData.put(newUserObject);
        }
        fileWriter.write(newUserData.toString());
        fileWriter.close();
    }

    /*
    Goes through supported languages aka from assets language.xml file.
    Adds languages to language list which is used in ArrayAdapter to show
    apps supported languages.
     */
    public List parseLanguage(Activity activity, List languageList) {

        try (InputStream ins = activity.getAssets().open("language.xml")){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("language");
            ins.close();

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String languageName = element.getElementsByTagName("name").item(0).getTextContent();
                    languageList.add(languageName);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return languageList;
    }


}
