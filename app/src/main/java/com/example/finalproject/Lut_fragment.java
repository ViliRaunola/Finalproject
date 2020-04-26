package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Lut_fragment extends Fragment {

    private ListView listView;
    public ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    public List<String> uni = new ArrayList<String>();
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
        View v = inflater.inflate(R.layout.lut, container, false);
        /*
        *
        *
        * */
        infoWin = (TextView)v.findViewById(R.id.infoWindow);
        infoWin.setText("LUT university is located in Lappeenranta.\nThere are 3 different ");
        universitySpinner = (Spinner)v.findViewById(R.id.university_spinner);
        uni.add("LUT");
        uni.add("Aalto");
        uni.add("Tampere");
        ArrayAdapter<String> ap = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, uni);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(ap);
        restaurants.add(laser);
        restaurants.add(buffet);
        restaurantSpinner = (Spinner)v.findViewById(R.id.restaurant_spinner);
        ArrayAdapter<Restaurant> arrayAdapter = new ArrayAdapter<Restaurant>(getActivity(), android.R.layout.simple_spinner_item, restaurants);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantSpinner.setAdapter(arrayAdapter);

        //Here is listView + array adapter for it. This listView is meant for food items.
        //Food items come from some xml file; not sure yet from where??!
        parseMenuFile();
        listView = (ListView) v.findViewById(R.id.listViewFood);
        ArrayAdapter<FoodItem> arrayAdapterListView = new ArrayAdapter<FoodItem>(getActivity(), android.R.layout.simple_list_item_1, laser.restaurantMenus);
        listView.setAdapter(arrayAdapterListView);

        parseUniversity();

        return v;
    }

    public void parseUniversity(){
        String output ="";

        try (InputStream ins = getContext().getAssets().open("university.xml")){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("university");
            System.out.println(nodeList.getLength());

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String name = element.getElementsByTagName("univeristyName").item(0).getTextContent();
                    String id = element.getElementsByTagName("id").item(0).getTextContent();
                    String xml = element.getElementsByTagName("restarantXml").item(0).getTextContent();
                    /*
                    System.out.println("###########################################"+ name + "###########################################");
                    System.out.println("###########################################"+ id + "###########################################");
                    System.out.println("###########################################"+ xml + "###########################################");
                     */
                    University university = new University(name, id);
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








        public void parseMenuFile(){
        String name = null;
        String price = null;
        int id = 0;
        try {
            XmlPullParserFactory parserFactory;
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getContext().getAssets().open("laseri.xml");
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
                                laser.restaurantMenus.add(fd);//tulee muuttaa
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
