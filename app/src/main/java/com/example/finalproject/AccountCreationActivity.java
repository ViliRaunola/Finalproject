package com.example.finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AccountCreationActivity extends AppCompatActivity {
    private Button confirmButton;
    private Button cancelButton;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText email;
    private EditText passwordConfirm;
    private Spinner home_uni_spinner;
    private String firstname_string;
    private String lastname_string;
    private String password_string;
    private String passwordConfirm_string;
    private String email_string;
    private String home_uni;
    private boolean checkEmail;
    private boolean checkPassword;
    private byte[] buffer;
    private int newUserId;
    private User user = User.getInstance();
    private ParseClass parseClass = ParseClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting correct language
        Language.getInstance().loadLocale(this);

        setContentView(R.layout.activity_account_creation);

        confirmButton = (Button)findViewById(R.id.confirmButton_account_creation);
        cancelButton = (Button)findViewById(R.id.cancelButton_account_creation);
        firstName = (EditText)findViewById(R.id.firstNameTextField_account_creation);
        password = (EditText)findViewById(R.id.passwordTextField_account_creation);
        passwordConfirm = (EditText)findViewById(R.id.confirmPasswordTextField_account_creation);
        email = (EditText)findViewById(R.id.emailTextField_account_creation);
        lastName = (EditText)findViewById(R.id.lastNameTextField_account_creation);
        home_uni_spinner = (Spinner)findViewById(R.id.homeUniversity_spinner_account_creation);

        //setting the actionbar title as current page
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.accountCreation_appBar));
        //create spinner from universityList

        //making array adapter for the home university spinner and setting it
        ArrayAdapter<University> ap = new ArrayAdapter<University>(this, android.R.layout.simple_list_item_1, parseClass.getUniversities());
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        home_uni_spinner.setAdapter(ap);

        //when pressing cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

    }

    /*
    *getting text from the text fields and spinner
    *checking if email and password details and creates new user
    */
    public void confirmPressed(View view)  {

        //get text from text fields and spinner
        home_uni = home_uni_spinner.getSelectedItem().toString();
        email_string = email.getText().toString();
        lastname_string = lastName.getText().toString();
        firstname_string = firstName.getText().toString();
        password_string = password.getText().toString();
        passwordConfirm_string = passwordConfirm.getText().toString();

        //check if password meets the minimum requirements for a password
        checkPassword = Security.passwordChecker(password_string);

        try {
            // reading emailsAndIds file
            buffer = readEmailsAndIds();

            //checking if email is already in use
            checkEmail = checkIfEmailInUse(email_string, buffer);

            //creating new userId
            newUserId = generateId(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //if email is already in use
        if (checkEmail) {
            Toast.makeText(this, getResources().getString(R.string.toast_emailAlreadyInUse), Toast.LENGTH_SHORT).show();

        //if password and confirm password are not the same
        }else if (!passwordConfirm_string.equals(password_string)){
            Toast.makeText(this, getResources().getString(R.string.toast_passwordAndConfirmationDontMatch), Toast.LENGTH_SHORT).show();
            password.setText("");
            passwordConfirm.setText("");

        //if password does not meet the minimum requirements for a password
        }else if (!checkPassword) {
            Toast.makeText(this,getResources().getString(R.string.toast_passwordRequirementsNotMet) , Toast.LENGTH_SHORT).show();
            password.setText("");
            passwordConfirm.setText("");

        }else {
            //create a new user
            user.setEmail(email_string);
            user.setFirstName(firstname_string);
            user.setLastName(lastname_string);
            user.setHomeUniversity(home_uni);

            //get secured password
            password_string = Security.getSecuredPassword(password_string, email_string);
            user.setPassword(password_string);
            user.setUserID(newUserId);

            //writing new user file and updating emailsAnsIds file
            try {
                writeNewUserJson();
                saveUserDataToEmailAndIdsFile();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            //go to login page
            startActivity(new Intent(AccountCreationActivity.this, LoginActivity.class));
            finish();
        }

    }

    //reading EmailsAndIds file
    public byte[] readEmailsAndIds() throws IOException {
        FileInputStream ins = new FileInputStream (new File(this.getFilesDir() +"/userData/EmailsAndIds.json"));
        int size = ins.available();
        final byte[] buffer = new byte[size];
        ins.read(buffer);
        ins.close();

        //returning the original file back as byte array
        return buffer;
    }

    //check if the email is already in use
    public boolean checkIfEmailInUse(String email, byte[] buffer) throws IOException, JSONException {

        //new string is original EmailsAndIds file
        String json = new String(buffer, "UTF-8");
        JSONArray originalUserData = new JSONArray(json);

        //going through original file and checking if the email is the same
        for (int i = 0; i < originalUserData.length(); i++) {
            JSONObject userObject = originalUserData.getJSONObject(i).getJSONObject("user");
            if (email.equals(userObject.getString("eMail"))){
                return true;
            }
        }
        return  false;
    }

    //generating userID for the new user
    public int generateId(byte[] buffer) throws UnsupportedEncodingException, JSONException {
        int userId = 0;

        //new string is original EmailsAndIds file
        String json = new String(buffer, "UTF-8");
        JSONArray originalUserData = new JSONArray(json);

        //going through original file and finding the largest userID there
        for (int i = 0; i < originalUserData.length(); i++) {
            JSONObject userObject = originalUserData.getJSONObject(i).getJSONObject("user");
            if (Integer.parseInt(userObject.get("userId").toString()) > userId){
                userId = Integer.parseInt(userObject.get("userId").toString());
            }
        }

        //adding new userID
        userId += 1;
        return  userId;
    }

    //writing json file for the new user
    public void writeNewUserJson() throws JSONException {

        //creating new JSONArray and JSONObject
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        //setting user object values to JSOnObject
        jsonObject.put("password", user.getPassword());
        jsonObject.put("userId", user.getUserID());
        jsonObject.put("firstName", user.getFirstName());
        jsonObject.put("lastName", user.getLastName());
        jsonObject.put("eMail", user.getEmail());
        jsonObject.put("homeUniversity", user.getHomeUniversity());
        jsonObject.put("adminStatus", false);
        jsonArray.put(jsonObject);

        try{
            String x = String.format(this.getFilesDir() + "/userData/User" + user.getUserID() + ".json");
            FileWriter fileWriter = new FileWriter(x);
            fileWriter.write(jsonArray.toString());
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //save user to email and ids file
    public void saveUserDataToEmailAndIdsFile() throws JSONException, IOException {

        //reading original file
        FileInputStream fis = new FileInputStream (new File(this.getFilesDir() + "/userData/EmailsAndIds.json"));
        int size = fis.available();
        final byte[] buffer = new byte[size];
        fis.read(buffer);
        fis.close();

        String json = new String(buffer, "UTF-8");
        JSONArray originalUserData = new JSONArray(json);
        JSONArray newUserData = new JSONArray();
        FileWriter fileWriter = new FileWriter((this.getFilesDir() + "/userData/EmailsAndIds.json"));

        //going through original EmailsAnsIds file and adding new user
        for (int i = 0; i < originalUserData.length(); i++) {
            JSONObject userObject = originalUserData.getJSONObject(i).getJSONObject("user");
            JSONObject oldUserObject = new JSONObject();
            oldUserObject.put("user", userObject);
            newUserData.put(oldUserObject);
        }

        //creating a new user JSONobject and adding it to the file
        JSONObject newUserObject = new JSONObject();
        newUserObject.put("userId", user.getUserID());
        newUserObject.put("eMail", user.getEmail());

        JSONObject userObject = new JSONObject();
        userObject.put("user",newUserObject);
        newUserData.put(userObject);

        //adding them to the end of the file
        fileWriter.append(newUserData.toString());
        fileWriter.close();
    }
}
