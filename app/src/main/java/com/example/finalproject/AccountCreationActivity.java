package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AccountCreationActivity extends AppCompatActivity {
    private Button confirm;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText email;
    private Spinner home_uni_spinner;
    private List<String> universityList = new ArrayList<String>();
    private String firstname_string;
    private String lastname_string;
    private String password_string;
    private String email_string;
    private String home_uni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);

        confirm = (Button)findViewById(R.id.confirm);
        firstName = (EditText)findViewById(R.id.firstNameTextField_account_creation);
        password = (EditText)findViewById(R.id.passwordTextField_account_creation);
        email = (EditText)findViewById(R.id.emailTextField_account_creation);
        lastName = (EditText)findViewById(R.id.lastNameTextField_account_creation);
        home_uni_spinner = (Spinner)findViewById(R.id.homeUniversitySpinner);

        //create spinner from universityList
        parseUniversity();
        ArrayAdapter<String> ap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, universityList);
        ap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        home_uni_spinner.setAdapter(ap);
    }
    public void confirmPressed(View view){

        //get text from textfields and spinner
        home_uni = home_uni_spinner.getSelectedItem().toString();
        email_string = email.getText().toString();
        lastname_string = lastName.getText().toString();
        firstname_string = firstName.getText().toString();
        password_string = password.getText().toString();

        //TODO tarkista onko sama email muuten
        //create a new user
        User user = new User();
        user.setEmail(email_string);
        user.setFirstName(firstname_string);
        user.setLastName(lastname_string);
        user.setHomeUniversity(home_uni);
        byte[] salt = Security.getSalt();
        password_string = Security.getSecuredPassword(password_string,salt);
        user.setPassword(password_string);
        user.setSalt(salt);
        //TODO USER ID generointi ja tallennus
        //TODO ja näiden tietojen kirjoitus tiedostoon

        startActivity(new Intent(AccountCreationActivity.this, LoginActivity.class));
        finish();
    }
    //parses "university.xml" to make a list from university names
    public void parseUniversity(){

        try (InputStream ins = this.getAssets().open("university.xml")){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("university");

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String uniName = element.getElementsByTagName("universityName").item(0).getTextContent();
                    universityList.add(uniName);
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
