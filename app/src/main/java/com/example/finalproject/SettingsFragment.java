package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    private Spinner languageSpinner;
    private Button changeLanguageButton;
    private int languagePosition;
    private List<String> languageList = new ArrayList<String>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        languageSpinner = (Spinner)v.findViewById(R.id.languageSpinner_settings);
        changeLanguageButton = (Button)v.findViewById(R.id.changeLanguageButton_settings);
        languageList.add("English");
        languageList.add("Finnish");
        ArrayAdapter<String> languageSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, languageList);
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageSpinnerAdapter);
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languagePosition = languageSpinner.getSelectedItemPosition();
                switch (languagePosition){
                    case 0:
                        break;
                    case 1:
                        break;
                }


            }
        });

        return v;
    }
}
