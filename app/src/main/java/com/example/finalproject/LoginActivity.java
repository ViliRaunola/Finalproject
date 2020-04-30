package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button createAccount;
    private EditText password;
    private EditText email;
    private String pw;
    private String em;
    private String fileEMail;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);
        createAccount = (Button)findViewById(R.id.createAccount);
        email = (EditText)findViewById(R.id.emailTextField_login);
        password = (EditText)findViewById(R.id.passwordTextField_login);
    }
    public void logInPressed(View view){
        em = email.getText().toString();
        pw = password.getText().toString();
        credentialsCheck(em, pw);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
    public void createNewAccountPressed(View view){
        startActivity(new Intent(LoginActivity.this, AccountCreationActivity.class));
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        this.moveTaskToBack(true);
    }
    //https://www.youtube.com/watch?v=y2xtLqP8dSQ,https://www.youtube.com/watch?v=h71Ia9iFWfI
    public void credentialsCheck(String email, String password){
        //TODO tiedoston avaus, luku ja salasanan ja sähköpostin tarkistus,jos väärin alla oleva viesti
        jsonParse(email, password);
        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
    }

    //Reads Users-json file and checks user's input (email and password) and compares them to the file parameters
    //If they both match a user class is created. 
    private void jsonParse(String userEMail, String userPassword){
        String json;
        try (InputStream ins = getBaseContext().getAssets().open("userData/Users")){
            int size = ins.available();
            byte[] buffer = new byte[size];
            ins.read(buffer);
            ins.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);

                System.out.println(object.getString("eMail"));
                if(object.getString("eMail").equals(userEMail) && object.getString("password").equals(userPassword)){
                    user.setEmail(userEMail);
                    user.setPassword(userPassword);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }



}
