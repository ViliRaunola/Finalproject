package com.example.finalproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SettingsFragment extends Fragment {
    private Spinner languageSpinner;
    private Button changeLanguageButton;
    private List<String> languageList = new ArrayList<String>();
    private MainActivity mainActivity;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        changeLanguageButton = (Button)v.findViewById(R.id.changeLanguageButton_settings);
        languageSpinner = (Spinner)v.findViewById(R.id.languageSpinner_settings);
        parseLanguage();
        final ArrayAdapter<String> languageSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, languageList);
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageSpinnerAdapter);

        if (Locale.getDefault().getLanguage().equals("en")){
            languageSpinner.setSelection(0);
        }else if (Locale.getDefault().getLanguage().equals("fi")) {
            languageSpinner.setSelection(1);
        }

        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = languageSpinner.getSelectedItemPosition();
                switch (position) {
                    case 0:
                        Language.getInstance().setLocale("en", getContext());
                        getActivity().recreate();
                        break;
                    case 1:
                        Language.getInstance().setLocale("fi", getContext());
                        getActivity().recreate();
                        break;
                }
            }
        });
        System.out.println(Locale.getDefault().getLanguage());
        return v;
    }

    public void parseLanguage() {

        try (InputStream ins = getActivity().getAssets().open("language.xml")){
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = documentBuilder.parse(ins);
            NodeList nodeList = xmlDoc.getDocumentElement().getElementsByTagName("language");

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    String uniName = element.getElementsByTagName("name").item(0).getTextContent();
                    languageList.add(uniName);
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

}
