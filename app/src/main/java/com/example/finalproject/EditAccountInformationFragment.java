package com.example.finalproject;

import android.content.Context;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EditAccountInformationFragment extends Fragment {
    private Button saveChanges;
    private Button cancelButton;
    private ArrayList<String> universities = new ArrayList<String>();
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
    private Context context;
    private String currentPasswordUser;
    User user = User.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.edit_account_information_fragment, container, false);
        context = this.getContext();

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
        parseUniversity();
        ArrayAdapter<String> universityArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, universities);
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

                try {
                    emailCheck = checkIfEmailInUse(emailInput);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //checking if the password and confirmation are the same
                if (!passwordInput.equals(passwordConfirmationInput)) {
                    Toast.makeText(getContext(), "Password does not match password confirmation", Toast.LENGTH_SHORT).show();
                    newPasswordEditText.setText("");
                    newPasswordConfirmationEditText.setText("");
                }else if (!Security.getSecuredPassword(currentpasswordInput, user.getEmail()).equals(currentPasswordUser)) {
                    Toast.makeText(getContext(), "Given password does not match your current password", Toast.LENGTH_SHORT).show();
                    currentPasswordConfirmationEditText.setText("");
                }else if (emailCheck && !user.getEmail().equals(emailInput)) {
                    Toast.makeText(getContext(), "User with that email already exists", Toast.LENGTH_SHORT).show();
                }else{

                    //setting new user data
                    user.setLastName(lastNameInput);
                    user.setFirstName(firstNameInput);
                    user.setHomeUniversityPos(homeUniversityPos);
                    System.out.println(homeUniversityPos + "             kotihomon paikka ========================================================================================¤");
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
                                modifyEmailsAndIds();
                                writeUserJson();
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                            //switching fragments
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountFragment()).commit();

                        }else {

                            newPasswordEditText.setText("");
                            newPasswordConfirmationEditText.setText("");
                            Toast.makeText(getContext(),"Your password does not contains all the required characters",Toast.LENGTH_SHORT).show();
                        }
                    //if user does not want to change their password
                    }else {
                        //Rewrites the changed user file
                        try {
                            modifyEmailsAndIds();
                            writeUserJson();
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

    //Vili!Raunola12345

    //TODO lisää parse luokkaan
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
    //TODO lisää parse luokkaan
    //https://howtodoinjava.com/library/json-simple-read-write-json-examples/
    public void modifyEmailsAndIds() throws JSONException, IOException {

        //reading original file
        FileInputStream ins = new FileInputStream (new File(context.getFilesDir() +"/userData/EmailsAndIds.json"));
        int size = ins.available();
        final byte[] buffer = new byte[size];
        ins.read(buffer);
        ins.close();

        String json = new String(buffer, "UTF-8");
        JSONArray originalUserData = new JSONArray(json);
        JSONArray newUserData = new JSONArray();
        FileWriter fileWriter = new FileWriter((context.getFilesDir() + "/userData/EmailsAndIds.json"));

        //goinng through original EmailsAnsIds file
        for (int i = 0; i < originalUserData.length(); i++) {
            JSONObject userObject = originalUserData.getJSONObject(i).getJSONObject("user");

            //changing new user email for the same user id
            if (Integer.parseInt(userObject.getString("userId")) == user.getUserID()) {
                userObject.put("eMail", user.getEmail());
            }

            //create a newUserObject and adding it to newUserData
            JSONObject newUserObject = new JSONObject();
            newUserObject.put("user", userObject);
            newUserData.put(newUserObject);
        }

        fileWriter.write(newUserData.toString());
        fileWriter.close();
    }
    //TODO lisää parse luokkaan
    //https://www.tutorialspoint.com/how-to-write-create-a-json-file-using-java
    public void writeUserJson() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        String password;
        //password = Security.getSecuredPassword(user.getPassword(),user.getEmail());
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(user.getPassword());
        //System.out.println(password);
        jsonObject.put("password", user.getPassword());
        jsonObject.put("userId", user.getUserID());
        jsonObject.put("firstName", user.getFirstName());
        jsonObject.put("lastName", user.getLastName());
        jsonObject.put("eMail", user.getEmail());
        jsonObject.put("homeUniversity", user.getHomeUniversity());
        jsonObject.put("adminStatus", user.getIsAdminUser());
        jsonArray.put(jsonObject);
        try{

            String x = String.format(context.getFilesDir() + "/userData/User" + user.getUserID() + ".json");
            System.out.println(x);
            FileWriter fileWriter = new FileWriter(x);
            fileWriter.write(jsonArray.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO lisää parse luokkaan
    public boolean checkIfEmailInUse(String email) throws IOException, JSONException {
        FileInputStream ins = new FileInputStream (new File(context.getFilesDir() +"/userData/EmailsAndIds.json"));
        int size = ins.available();
        final byte[] buffer = new byte[size];
        ins.read(buffer);
        ins.close();

        String json = new String(buffer, "UTF-8");
        JSONArray originalUserData = new JSONArray(json);

        for (int i = 0; i < originalUserData.length(); i++) {
            JSONObject userObject = originalUserData.getJSONObject(i).getJSONObject("user");
            if (email.equals(userObject.getString("eMail"))){
                return true;
            }

        }
        return  false;
    }
}
