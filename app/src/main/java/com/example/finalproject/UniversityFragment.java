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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class UniversityFragment extends Fragment {

    private ListView listView;
    public ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private TextView infoWin;
    public ArrayList<University> universities = new ArrayList<University>();

    Restaurant laser = new Restaurant("Laseri");
    Restaurant buffet = new Restaurant("Lut Buffet");
    Restaurant yo = new Restaurant("Ylioppilastalo");

    private Spinner restaurantSpinner;
    private Spinner universitySpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.university, container, false);
        infoWin = (TextView)v.findViewById(R.id.infoWindow);
        universitySpinner = (Spinner)v.findViewById(R.id.university_spinner);
        parseUniversity();
        ArrayAdapter<University> ap = new ArrayAdapter<University>(getActivity(), android.R.layout.simple_list_item_1, universities);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(ap);

        //On select adds specific restaurants to restaurantSpinner depending the selected university
        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                restaurants.clear();
                int uniPosition = position;

                parseRestaurantsMenu(position);
                restaurantSpinner = (Spinner)v.findViewById(R.id.restaurant_spinner);
                ArrayAdapter<Restaurant> arrayAdapter = new ArrayAdapter<Restaurant>(getActivity(), android.R.layout.simple_spinner_item, restaurants);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                restaurantSpinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Here is listView + array adapter for it. This listView is meant for food items.
        //Food items come from some xml file; not sure yet from where??!
        parseMenuFile();

        /*
        listView = (ListView) v.findViewById(R.id.listViewFood);
        ArrayAdapter<FoodItem> arrayAdapterListView = new ArrayAdapter<FoodItem>(getActivity(), android.R.layout.simple_list_item_1, laser.restaurantMenus);
        listView.setAdapter(arrayAdapterListView);
        */
        // TODO add listview


        return v;
    }


    //Parses "university.xml" and creates University objects based on .xml parameters. Adds these new University objects to "universities"-ArrayList.
    //This ArrayList is shown in university_spinner.
    public void parseUniversity(){

        try (InputStream ins = getContext().getAssets().open("university.xml")){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("university");
            System.out.println(nodeList.getLength());

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String uniName = element.getElementsByTagName("universityName").item(0).getTextContent();
                    String uniId = element.getElementsByTagName("id").item(0).getTextContent();
                    String resXMLFileName = element.getElementsByTagName("restaurantXml").item(0).getTextContent();
                    String uniInfoText = element.getElementsByTagName("restaurantInfo").item(0).getTextContent();
                    /*
                    System.out.println("###########################################"+ name + "###########################################");
                    System.out.println("###########################################"+ id + "###########################################");
                    System.out.println("###########################################"+ xml + "###########################################");
                     */
                    University university = new University(uniName, uniId, uniInfoText);
                    university.addToRestaurants(resXMLFileName);
                    universities.add(university);

                }
            }
            System.out.println("###########" + universities.size()+ "############");
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
                    System.out.println(nodeList.getLength());

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            String resName = element.getElementsByTagName("restaurantName").item(0).getTextContent();
                            String resMenuName = element.getElementsByTagName("restaurantMenu").item(0).getTextContent();
                            Restaurant restaurant = new Restaurant(resName);
                            restaurant.addToRestaurantMenus(resMenuName);
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

    public void parseMenuFile(){
    String name = null;
    String price = null;
    int id = 0;
    try {
        XmlPullParserFactory parserFactory;
        parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = parserFactory.newPullParser();
        InputStream is = getContext().getAssets().open("laseriMenu.xml");
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT){
            String tag = null;
            FoodItem fd = null;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    tag = parser.getName();

                    if ("food".equals(tag)){
                            System.out.println(tag);

                    }else{
                        if ("name".equals(tag)){
                            name = parser.nextText();
                            System.out.println(name);
                        } else if ("price".equals(tag)){
                            price = parser.nextText();
                            System.out.println(price);
                        } else if ("id".equals(tag)){
                            id = Integer.parseInt(parser.nextText());
                            System.out.println(id);
                            fd = new FoodItem(name, price, id);
                            //laser.restaurantMenus.add(fd);//    TODO tulee muuttaa
                        }
                    }
                    break;
            }
            eventType  = parser.next();

        }
    } catch (IOException e) {
        e.printStackTrace();
    } catch (XmlPullParserException e) {
        e.printStackTrace();
    }finally{
        System.out.println("*********DONE*******");
    }

}





}
