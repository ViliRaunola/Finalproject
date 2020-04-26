package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);

    }
    public void logInPressed(View view){
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
}
