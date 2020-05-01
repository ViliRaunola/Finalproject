package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AccountFragment extends Fragment {
    private Button editButton;
    private EditText email;
    private EditText password;
    private EditText lastName;
    private EditText firstName;
    private ArrayList<University> homeUniversity;
    private Spinner universitySpinner;

    EditAccountInformationFragment editAccountInformationFragment = new EditAccountInformationFragment();
    User user = User.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account, container, false);

        universitySpinner = (Spinner) v.findViewById(R.id.universitySpinner_Account);
        editButton = (Button)v.findViewById(R.id.editButton);
        email = (EditText)v.findViewById(R.id.emailTextField_account_creation);
        firstName = (EditText)v.findViewById(R.id.firstNameTextField_account_creation);
        lastName = (EditText)v.findViewById(R.id.lastNameTextField_account_creation);
        password = (EditText)v.findViewById(R.id.passwordTextField_account_creation);

        try {
            homeUniversity = (ArrayList<University>) getArguments().getSerializable("key2");
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayAdapter<University> ap = new ArrayAdapter<University>(getActivity(), android.R.layout.simple_list_item_1, homeUniversity);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(ap);
        universitySpinner.setEnabled(false);

        universitySpinner.setSelection(user.getHomeUniversity());

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        password.setText("************");


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,editAccountInformationFragment).addToBackStack("account_fragment").commit();
                Toast.makeText(getContext(),"Account Edit fragment", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
