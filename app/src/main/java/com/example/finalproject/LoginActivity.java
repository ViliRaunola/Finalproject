package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button createAccount;
    private EditText password;
    private EditText email;
    private String pw;
    private String em;
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
    public void credentialsCheck(String email, String password){
        //TODO tiedoston avaus, luku ja salasanan ja sähköpostin tarkistus,jos väärin alla oleva viesti
        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
    }
}
