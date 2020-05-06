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
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the language.
        Language.getInstance().loadLocale(this);
        setContentView(R.layout.activity_login);

        //Getting this activity's context to a variable.
        context = this;

        parseClass.parseUniversity(context);
        login = (Button) findViewById(R.id.login);
        createAccount = (Button) findViewById(R.id.createAccount);
        email = (EditText) findViewById(R.id.emailTextField_login);
        password = (EditText) findViewById(R.id.passwordTextField_login);
        languageSpinner = (Spinner)findViewById(R.id.languageSpinner_login);
        languageList.add(getResources().getString(R.string.loginScreen_changeLanguage));

        //Creating language spinner.
        languageList = parseClass.parseLanguage(this, languageList);
        final ArrayAdapter<String> languageSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languageList);
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageSpinnerAdapter);

        //Set selection to always show change language/vaihda kieli.
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
                Toast.makeText(getBaseContext(), getResources().getString(R.string.toast_invalidCredentials), Toast.LENGTH_SHORT).show();
            }
                }
        });
    }

    //Pressing "create new account" opens up new activity called AccountCreationActivity
    public void createNewAccountPressed(View view) {
        startActivity(new Intent(LoginActivity.this, AccountCreationActivity.class));
    }

    //When phones own back button is pressed App is put to background.
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    /*
    https://www.youtube.com/watch?v=y2xtLqP8dSQ,https://www.youtube.com/watch?v=h71Ia9iFWfI
    This method calls jsonParse-method to check if user's id and password are already in a file.
    If id and password are found user cant log in.
    If id and password are found and the user is admin he skips Authentication.
    If user isn't admin he goes to authentication activity.
     */
    public void credentialsCheck(String id, String password) {
        check = jsonParse(id, password);
        if (check == 0) {
            Toast.makeText(this, getResources().getString(R.string.toast_invalidCredentials), Toast.LENGTH_SHORT).show();
        } else {
            if(User.getInstance().getIsAdminUser()){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }else{
                startActivity(new Intent(LoginActivity.this, Authentication.class));
            }
        }
    }

    //Reads Users-json file and checks user's input (email and password) and compares them to the file parameters
    //If they both match a user class is created. 
    private int jsonParse(String id, String userPassword) {
        String json;
        try (FileInputStream ins = new FileInputStream(context.getFilesDir() + "/userData/User" + id + ".json")){

            //Reads user's file into buffer.
            int size = ins.available();
            byte[] buffer = new byte[size];
            ins.read(buffer);
            ins.close();

            //Buffer is read into json array.
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            //Every item in jsonArray is read through
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);

                //If user's id and password equals what is in jsonArray a new user instance is created
                if (object.getString("userId").equals(id) && object.getString("password").equals(userPassword)) {

                    //Giving user basic values from the file
                    User user = User.getInstance();
                    user.setAdminUser(object.getBoolean("adminStatus"));
                    user.setUserID(Integer.parseInt(id));
                    user.setEmail(object.getString("eMail"));
                    user.setPassword(userPassword);
                    homeUniversity = object.getString("homeUniversity");

                    //Giving user's homeuniversitys position
                    //by comparing homeunversity name to parseClass's list's position.
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

                    /*
                    These two if and for loops are to read lists in json file and giving their
                    items to user instance.
                     */
                    if(!object.isNull("downVoted")){
                        JSONArray downVoteJson = object.getJSONArray("downVoted");
                        for(int d = 0; d < downVoteJson.length(); d++ ){
                            JSONObject downVoteList = downVoteJson.getJSONObject(d);
                            String reviewId = downVoteList.getString("reviewId");
                            user.getDownVotedList().add(reviewId);
                        }
                    }
                    if(!object.isNull("upVoted")){
                        JSONArray upVoteJson = object.getJSONArray("upVoted");
                        for(int d = 0; d < upVoteJson.length(); d++ ){
                            JSONObject upVoteList = upVoteJson.getJSONObject(d);
                            String reviewId = upVoteList.getString("reviewId");
                            user.getUpVotedList().add(reviewId);
                        }
                    }
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

    //Returns user id by comparing user's email in EmailsAndIds json file.
    private String jsonParseEmailsAndIds(String userEMail) {
        String id = "";
        String json;

        //Reads file in to buffer.
        try (FileInputStream ins = new FileInputStream (new File(context.getFilesDir() +"/userData/EmailsAndIds.json"))) {
            int size = ins.available();
            byte[] buffer = new byte[size];
            ins.read(buffer);
            ins.close();

            //Buffer read into jsonArray
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            //Goes through every item in jsonArray and compares their eMail to what user gave.
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

}
