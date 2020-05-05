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
    private int position;
    private List<String> languageList = new ArrayList<String>();

    ParseClass parseClass = ParseClass.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        changeLanguageButton = (Button)v.findViewById(R.id.changeLanguageButton_settings);
        languageSpinner = (Spinner)v.findViewById(R.id.languageSpinner_settings);
        languageList = parseClass.parseLanguage(getActivity(), languageList);
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

}
