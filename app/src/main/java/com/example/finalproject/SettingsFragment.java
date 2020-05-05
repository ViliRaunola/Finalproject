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
import java.util.Locale;

public class SettingsFragment extends Fragment {
    private Spinner languageSpinner;
    private Button changeLanguageButton;
    private int position;
    private List<String> languageList = new ArrayList<String>();
    ParseClass parseClass = ParseClass.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);

        changeLanguageButton = (Button)v.findViewById(R.id.changeLanguageButton_settings);
        languageSpinner = (Spinner)v.findViewById(R.id.languageSpinner_settings);

        //reading language xml and getting list for available languages
        languageList = parseClass.parseLanguage(getActivity(), languageList);

        //setting adapter for language spinner using language list
        ArrayAdapter<String> languageSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, languageList);
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageSpinnerAdapter);

        //setting language spinner position to current app language
        if (Locale.getDefault().getLanguage().equals("en")){
            languageSpinner.setSelection(0);
        }else if (Locale.getDefault().getLanguage().equals("fi")) {
            languageSpinner.setSelection(1);
        }

        //changing language based on position
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting current spinner position
                position = languageSpinner.getSelectedItemPosition();

                //setting new language and recreating the activity
                //https://www.youtube.com/watch?v=zILw5eV9QBQ source for changing the language
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
        return v;
    }

}
