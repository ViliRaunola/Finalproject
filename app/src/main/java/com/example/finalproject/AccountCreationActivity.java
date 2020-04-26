package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountCreationActivity extends AppCompatActivity {
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);
        confirm = (Button)findViewById(R.id.confirm);
    }
    public void confirmPressed(View view){
        startActivity(new Intent(AccountCreationActivity.this, LoginActivity.class));
        finish();
    }
}
