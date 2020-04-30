package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;



public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button createAccount;
    private EditText password;
    private EditText email;
    private String pw;
    private String em;
    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        createAccount = (Button) findViewById(R.id.createAccount);
        email = (EditText) findViewById(R.id.emailTextField_login);
        password = (EditText) findViewById(R.id.passwordTextField_login);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                em = email.getText().toString();
                pw = password.getText().toString();
                pw = Security.getSecuredPassword(pw, em);
                credentialsCheck(em, pw);
                if (check == 0){
                    password.setText("");
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

    //https://www.youtube.com/watch?v=y2xtLqP8dSQ,https://www.youtube.com/watch?v=h71Ia9iFWfI
    public void credentialsCheck(String email, String password) {
        //TODO tiedoston avaus, luku ja salasanan ja sähköpostin tarkistus,jos väärin alla oleva viesti
        check = jsonParse(email, password);
        if (check == 0) {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();

        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    //Reads Users-json file and checks user's input (email and password) and compares them to the file parameters
    //If they both match a user class is created. 
    private int jsonParse(String userEMail, String userPassword) {

        String json;
        try (InputStream ins = getBaseContext().getAssets().open("userData/Users")) {
            int size = ins.available();
            byte[] buffer = new byte[size];
            ins.read(buffer);
            ins.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                System.out.println(object.getString("eMail"));
                if (object.getString("eMail").equals(userEMail) && object.getString("password").equals(userPassword)) {

                    User user = User.getInstance();
                    user.setEmail(userEMail);
                    user.setPassword(userPassword);
                    user.setHomeUniversity(object.getString("homeUniversity"));
                    user.setUserID(Integer.parseInt(object.getString("userId")));
                    user.setLastName(object.getString("lastName"));
                    user.setFirstName(object.getString("firstName"));
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
}