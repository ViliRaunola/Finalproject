package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Lut_fragment extends Fragment {

    private ListView listView;
    public ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

    Restaurant laser = new Restaurant("LASER");
    Restaurant buffet = new Restaurant("BUFFETTI SAATANA");

    private Spinner restaurant_spinner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lut, container, false);
        restaurants.add(laser);
        restaurants.add(buffet);
        restaurant_spinner = (Spinner)v.findViewById(R.id.restaurant_spinner);
        ArrayAdapter<Restaurant> arrayAdapter = new ArrayAdapter<Restaurant>(getActivity(), android.R.layout.simple_spinner_item, restaurants);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurant_spinner.setAdapter(arrayAdapter);

        //Here is listView + array adapter for it. This listView is meant for food items.
        //Food items come from some xml file; not sure yet from where??!
        listView = (ListView) v.findViewById(R.id.listViewFood);
        ArrayAdapter<String> arrayAdapterListView = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, HERECOMESJESSES_ARRAYLIST);
        listView.setAdapter(arrayAdapterListView);
        return v;
    }




}
