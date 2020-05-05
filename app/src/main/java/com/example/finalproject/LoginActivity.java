package com.example.finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button createAccount;
    private EditText password;
    private EditText email;
    private Spinner languageSpinner;
    private String pw;
    private String em;
    private String id;
    private Context context;
    private int universitiesPositionCompare;
    private String homeUniversity;
    private int check = 0;
    private List<String> languageList = new ArrayList<String>();
    ParseClass parseClass = ParseClass.getInstance();

    // LoginPasswordTestiHommma Vili!Raunola12345
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the language
        Language.getInstance().loadLocale(this);
        setContentView(R.layout.activity_login);


        context = this;
        parseClass.parseUniversity(context);
        login = (Button) findViewById(R.id.login);
        createAccount = (Button) findViewById(R.id.createAccount);
        email = (EditText) findViewById(R.id.emailTextField_login);
        password = (EditText) findViewById(R.id.passwordTextField_login);

        languageSpinner = (Spinner)findViewById(R.id.languageSpinner_login);
        languageList.add(getResources().getString(R.string.loginScreen_changeLanguage));

        //creating language spinner
        parseLanguage();
        final ArrayAdapter<String> languageSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languageList);
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageSpinnerAdapter);

        //set selection to always show change language/vaihda kieli
        languageSpinner.setSelection(0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.common_loginButton));
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        Language.getInstance().setLocale("en", context);
                        languageSpinner.setSelection(0);
                        recreate();
                        break;
                    case 2:
                        Language.getInstance().setLocale("fi", context);
                        languageSpinner.setSelection(0);
                        recreate();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    em = email.getText().toString();
                    pw = password.getText().toString();
                    pw = Security.getSecuredPassword(pw, em);
                    id = jsonParseEmailsAndIds(em);
                    if (!id.isEmpty()) {
                        credentialsCheck(id, pw);

                        if (check == 0) {
                            password.setText("");
                        }
                    } else {
                        password.setText("");
                        Toast.makeText(getBaseContext(), "We didn't find your account. Please create a new one.", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }


    public void createNewAccountPressed(View view) {
        startActivity(new Intent(LoginActivity.this, AccountCreationActivity.class));
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        this.moveTaskToBack(true);
    }

    //TODO lisää parse luokkaan
    //https://www.youtube.com/watch?v=y2xtLqP8dSQ,https://www.youtube.com/watch?v=h71Ia9iFWfI
    public void credentialsCheck(String id, String password) {
        //TODO tiedoston avaus, luku ja salasanan ja sähköpostin tarkistus,jos väärin alla oleva viesti
        check = jsonParse(id, password);
        if (check == 0) {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        } else {
            if(User.getInstance().getIsAdminUser()){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }else{
                startActivity(new Intent(LoginActivity.this, Authentication.class));
            }
        }
    }
    //TODO lisää parse luokkaan
    //Reads Users-json file and checks user's input (email and password) and compares them to the file parameters
    //If they both match a user class is created. 
    private int jsonParse(String id, String userPassword) {
        String json;
        try (FileInputStream ins = new FileInputStream(context.getFilesDir() + "/userData/User" + id + ".json")){

            int size = ins.available();
            byte[] buffer = new byte[size];
            ins.read(buffer);
            ins.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                if (object.getString("userId").equals(id) && object.getString("password").equals(userPassword)) {

                    User user = User.getInstance();
                    user.setAdminUser(object.getBoolean("adminStatus"));
                    user.setUserID(Integer.parseInt(id));
                    user.setEmail(object.getString("eMail"));
                    user.setPassword(userPassword);
                    homeUniversity = object.getString("homeUniversity");
                    //parseUniversity(homeUniversity);
                    String uniName = homeUniversity;
                    for(University u : parseClass.getUniversities()){
                        if (uniName.equals(u.toString())) {
                            universitiesPositionCompare = i;
                        }
                    }
                    user.setHomeUniversity(homeUniversity);
                    user.setHomeUniversityPos(universitiesPositionCompare);
                    user.setLastName(object.getString("lastName"));
                    user.setFirstName(object.getString("firstName"));

                    if(!object.isNull("downVoted")){
                        JSONArray downVoteJson = object.getJSONArray("downVoted");
                        for(int d = 0; d < downVoteJson.length(); d++ ){
                            JSONObject downVoteList = downVoteJson.getJSONObject(d);
                            String reviewId = downVoteList.getString("reviewId");
                            user.getDownVotedList().add(reviewId);
                        }
                    }

                    if(!object.isNull("upVoted")){
                        System.out.println("???????????????????????????????????????????");
                        JSONArray upVoteJson = object.getJSONArray("upVoted");
                        for(int d = 0; d < upVoteJson.length(); d++ ){
                            JSONObject upVoteList = upVoteJson.getJSONObject(d);
                            String reviewId = upVoteList.getString("reviewId");
                            System.out.println(reviewId + "???????????????????????????????????????????");
                            user.getUpVotedList().add(reviewId);
                        }
                    }
                    System.out.println("???????????????????????????????????????????jälkeen");

                    return 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //TODO lisää parse luokkaan
    private String jsonParseEmailsAndIds(String userEMail) {
        String id = "";
        String json;

        try (FileInputStream ins = new FileInputStream (new File(context.getFilesDir() +"/userData/EmailsAndIds.json"))) {
            int size = ins.available();
            byte[] buffer = new byte[size];
            ins.read(buffer);
            ins.close();
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i).getJSONObject("user");
                if (object.getString("eMail").equals(userEMail)){
                    id = object.getString("userId");
                    return id;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    /*
    //TODO lisää parse luokkaan
    public void parseUniversity(String hU){
        try (InputStream ins = context.getAssets().open("university.xml")){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("university");

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String uniName = element.getElementsByTagName("universityName").item(0).getTextContent();
                    if (uniName.equals(hU)) {
                        universitiesPositionCompare = i;
                    }

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

     */
    public void parseLanguage() {

        try (InputStream ins = this.getAssets().open("language.xml")){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("language");

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String uniName = element.getElementsByTagName("name").item(0).getTextContent();
                    languageList.add(uniName);
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
