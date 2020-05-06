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
import org.json.JSONException;
import java.io.IOException;

public class EditAccountInformationFragment extends Fragment {
    private Button saveChanges;
    private Button cancelButton;
    private Spinner homeUniversity_spinner;
    private EditText emailEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText newPasswordEditText;
    private EditText newPasswordConfirmationEditText;
    private EditText currentPasswordConfirmationEditText;
    private String emailInput;
    private String firstNameInput;
    private String lastNameInput;
    private String passwordInput;
    private String passwordConfirmationInput;
    private String currentpasswordInput;
    private int homeUniversityPos;
    private boolean checkPassword;
    private boolean emailCheck;
    private String currentPasswordUser;
    User user = User.getInstance();
    ParseClass parseClass = ParseClass.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.edit_account_information_fragment, container, false);

        saveChanges = (Button)v.findViewById(R.id.saveChangesButton_editAccountInformationFragment);
        cancelButton = (Button)v.findViewById(R.id.account_information_edit_cancel_button);
        homeUniversity_spinner = (Spinner)v.findViewById(R.id.homeUniversitySpinner_editAccountInformationFragment);
        emailEditText = (EditText)v.findViewById(R.id.emailEditText_editAccountInformationFragment);
        firstNameEditText = (EditText)v.findViewById(R.id.firstNameEditText_editAccountInformationFragment);
        lastNameEditText = (EditText)v.findViewById(R.id.lastNameEditText_editAccountInformationFragment);
        newPasswordEditText = (EditText)v.findViewById(R.id.newPasswordEditText_editAccountInformationFragment);
        newPasswordConfirmationEditText = (EditText)v.findViewById(R.id.newPasswordConfirmationEditText_editAccountInformationFragment);
        currentPasswordConfirmationEditText = (EditText)v.findViewById(R.id.currentPasswordConfirmation_editAccountInformationFragment);


        //set textfields to user data
        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        emailEditText.setText(user.getEmail());

        //get current password to compare it with given current password to editText
        currentPasswordUser = user.getPassword();

        //parsing university xml and setting home university spinner adapter
        ArrayAdapter<University> universityArrayAdapter = new ArrayAdapter<University>(getContext(), android.R.layout.simple_list_item_1, parseClass.getUniversities());
        universityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeUniversity_spinner.setAdapter(universityArrayAdapter);
        homeUniversity_spinner.setSelection(user.getHomeUniversityPos());


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //edittext and spinner item to string
                emailInput = emailEditText.getText().toString();
                firstNameInput = firstNameEditText.getText().toString();
                lastNameInput = lastNameEditText.getText().toString();
                passwordInput = newPasswordEditText.getText().toString();
                passwordConfirmationInput = newPasswordConfirmationEditText.getText().toString();
                homeUniversityPos = homeUniversity_spinner.getSelectedItemPosition();
                currentpasswordInput = currentPasswordConfirmationEditText.getText().toString();

                //checking if email already in use
                try {
                    emailCheck = parseClass.checkIfEmailInUse(emailInput, getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //checking if the password and confirmation are the same
                if (!passwordInput.equals(passwordConfirmationInput)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_passwordAndConfirmationDontMatch), Toast.LENGTH_SHORT).show();
                    newPasswordEditText.setText("");
                    newPasswordConfirmationEditText.setText("");
                //checking if given password matches current one
                }else if (!Security.getSecuredPassword(currentpasswordInput, user.getEmail()).equals(currentPasswordUser)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_wrongPassword), Toast.LENGTH_SHORT).show();
                    currentPasswordConfirmationEditText.setText("");
                //checking if email is same
                }else if (emailCheck && !user.getEmail().equals(emailInput)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_emailAlreadyInUse), Toast.LENGTH_SHORT).show();
                }else{

                    //setting new user data
                    user.setLastName(lastNameInput);
                    user.setFirstName(firstNameInput);
                    user.setHomeUniversityPos(homeUniversityPos);
                    user.setHomeUniversity(homeUniversity_spinner.getSelectedItem().toString());
                    user.setEmail(emailInput);

                    //check if user wants to change their password
                    if ((!passwordInput.isEmpty()) && (!passwordConfirmationInput.isEmpty())) {

                        //check if the password  contains all the required characters
                        checkPassword = Security.passwordChecker(passwordInput);

                        if (checkPassword) {

                            //getting secured password
                            passwordInput = Security.getSecuredPassword(passwordInput,emailInput);
                            user.setPassword(passwordInput);

                            //Rewrites the changed user file
                            try {
                                parseClass.modifyEmailsAndIds(getContext(), user);
                                parseClass.writeUserJson(getContext(), user);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                            //switching fragments
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountFragment()).commit();

                        //if password does not contain all th required characters
                        }else {

                            newPasswordEditText.setText("");
                            newPasswordConfirmationEditText.setText("");
                            Toast.makeText(getContext(),getResources().getString(R.string.toast_passwordRequirementsNotMet),Toast.LENGTH_SHORT).show();
                        }
                    //if user does not want to change their password
                    }else {
                        //Rewrites the changed user file
                        try {
                            parseClass.modifyEmailsAndIds(getContext(), user);
                            parseClass.writeUserJson(getContext(), user);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        //switching fragments
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountFragment()).commit();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountFragment()).commit();
            }
        });
        return v;
    }

}
