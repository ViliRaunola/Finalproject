package com.example.finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Authentication extends AppCompatActivity {
    private EditText randomNumberEditText;
    private TextView randomNumberTextView;
    private Button cancelButton;
    private Button loginButton;
    private String randomNumber;
    private String numberInput;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the current language
        Language.getInstance().loadLocale(this);
        setContentView(R.layout.activity_authentication);

        //getting the context
        context = this;

        //setting the action bar title as current page
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.authenticationView_toolbar));

        randomNumberEditText = (EditText) findViewById(R.id.randomNumberInputEditText);
        randomNumberTextView = (TextView) findViewById(R.id.randomNumberTextView);
        cancelButton = (Button) findViewById(R.id.cancelButton_authentication);
        loginButton = (Button) findViewById(R.id.loginButton_authentication);

        //creating a random six digit number
        randomNumber = Long.toString(Math.round(Math.random() * 900000 + 100000));
        randomNumberTextView.setText(randomNumber);

        //changing the activity if user has inputted the correct number combination
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the inputted number
                numberInput = randomNumberEditText.getText().toString();

                //if correct changes and enters the app
                if (numberInput.equals(randomNumber)) {
                    startActivity(new Intent(Authentication.this, MainActivity.class));
                    finish();

                }else {
                    Toast.makeText(context,getResources().getString(R.string.toast_wrongAuthenticationNumber),Toast.LENGTH_SHORT).show();
                }
            }
        });

        //going to previous page
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
