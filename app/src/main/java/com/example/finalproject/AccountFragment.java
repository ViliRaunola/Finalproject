package com.example.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class AccountFragment extends Fragment {
    private Button editButton;
    private EditText email;
    private EditText password;
    private EditText lastName;
    private EditText firstName;
    private EditText homeUniText;
    User user = User.getInstance();
    EditAccountInformationFragment editAccountInformationFragment = new EditAccountInformationFragment();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account, container, false);

        editButton = (Button)v.findViewById(R.id.editButton_account_fragment);
        email = (EditText)v.findViewById(R.id.emailEditText_account_fragment);
        firstName = (EditText)v.findViewById(R.id.firstNameEditText_account_fragment);
        lastName = (EditText)v.findViewById(R.id.lastNameEditText_account_fragment);
        password = (EditText)v.findViewById(R.id.passwordEditText_account_fragment);
        homeUniText = (EditText) v.findViewById(R.id.homeUniversityEditText_account_fragment);

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        password.setText("************");
        homeUniText.setText(user.getHomeUniversity());


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,editAccountInformationFragment).addToBackStack("account_fragment").commit();
                Toast.makeText(getContext(),"Account Edit fragment", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        password.setText("************");
        homeUniText.setText(user.getHomeUniversity());
    }
}
