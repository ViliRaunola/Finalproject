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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EditAccountInformationFragment extends Fragment {
    private Button saveChanges;
    private ArrayList<String> universities = new ArrayList<String>();
    private Spinner homeUniversity_spinner;
    private EditText emailEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmationEditText;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String passwordConfirmation;
    private String homeUniversity_string;
    private boolean checkPassword;
    User user = User.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.edit_account_information_fragment, container, false);
        saveChanges = (Button)v.findViewById(R.id.saveChangesButton_editAccountInformationFragment);
        homeUniversity_spinner = (Spinner)v.findViewById(R.id.homeUniversitySpinner_editAccountInformationFragment);
        emailEditText = (EditText)v.findViewById(R.id.emailEditText_editAccountInformationFragment);
        firstNameEditText = (EditText)v.findViewById(R.id.firstNameEditText_editAccountInformationFragment);
        lastNameEditText = (EditText)v.findViewById(R.id.lastNameEditText_editAccountInformationFragment);
        passwordEditText = (EditText)v.findViewById(R.id.passwordEditText_editAccountInformationFragment);
        passwordConfirmationEditText = (EditText)v.findViewById(R.id.passwordConfirmationEditText_editAccountInformationFragment);


        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        emailEditText.setText(user.getEmail());


        parseUniversity();
        ArrayAdapter<String> universityArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, universities);
        universityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeUniversity_spinner.setAdapter(universityArrayAdapter);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEditText.getText().toString();
                firstName = firstNameEditText.getText().toString();
                lastName = lastNameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                passwordConfirmation = passwordConfirmationEditText.getText().toString();
                homeUniversity_string = homeUniversity_spinner.getSelectedItem().toString();

                if (!password.equals(passwordConfirmation)){
                    Toast.makeText(getContext(),"Password does not match password confirmation",Toast.LENGTH_SHORT).show();
                    passwordEditText.setText("");
                    passwordConfirmationEditText.setText("");

                }else{

                    user.setLastName(lastName);
                    user.setFirstName(firstName);
                    user.setHomeUniversity(homeUniversity_string);
                    user.setEmail(email);

                    if ((!password.isEmpty()) && (!passwordConfirmation.isEmpty())) {
                        checkPassword = Security.passwordChecker(password);

                        if (checkPassword) {
                            password = Security.getSecuredPassword(password,email);
                            user.setPassword(password);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountFragment()).commit();
                        }else {
                            passwordEditText.setText("");
                            passwordConfirmationEditText.setText("");
                            Toast.makeText(getContext(),"Your password does not contains all the required characters",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        System.out.println(user.getFirstName());
        return v;
    }
    //parses "university.xml" to make a list from university names
    public void parseUniversity() {

        try (InputStream ins = getActivity().getAssets().open("university.xml")){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("university");

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String uniName = element.getElementsByTagName("universityName").item(0).getTextContent();
                    System.out.println(uniName);
                    universities.add(uniName);
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

}
