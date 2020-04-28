package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {
    private Button editButton;
    private EditText email;
    private EditText password;
    private EditText lastName;
    private EditText firstName;
    private EditText homeUniversity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account, container, false);
        editButton = (Button)v.findViewById(R.id.editButton);
        email = (EditText)v.findViewById(R.id.emailTextField_account_creation);
        firstName = (EditText)v.findViewById(R.id.firstNameTextField_account_creation);
        lastName = (EditText)v.findViewById(R.id.lastNameTextField_account_creation);
        password = (EditText)v.findViewById(R.id.passwordTextField_account_creation);
        homeUniversity = (EditText)v.findViewById(R.id.homeUnniversityTextField_account_fragment);
        firstName.setText("Vili");
        lastName.setText("Raunola");
        email.setText("Keijo.keijo@keijo.com");
        password.setText("********");
        homeUniversity.setText("LUT");
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Account Edit fragment", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
