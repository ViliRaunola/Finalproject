package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {
    private Button editButton;
    private EditText email;
    private EditText password;
    private EditText lastName;
    private EditText firstName;
    private EditText homeUniText;
    private User user = User.getInstance();
    private EditAccountInformationFragment editAccountInformationFragment = new EditAccountInformationFragment();

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

        //setting users information
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        password.setText("************");
        homeUniText.setText(user.getHomeUniversity());

        //changing fragment to editing account fragment
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,editAccountInformationFragment).commit();
            }
        });
        return v;
    }

    //setting user information when resuming fragment
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
