package com.example.finalproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class Language extends Application {

    private static Language language = new Language();
    public static Language getInstance(){return language;}
    private static String lang;


    @SuppressWarnings("deprecation")
    public void setLocale(String language, Context cont){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = cont.getResources().getConfiguration();
        config.locale = locale;
        cont.getResources().updateConfiguration(config, cont.getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = cont.getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_language", language);
        editor.apply();
    }
    public void loadLocale(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("My_language","");
        setLocale(language, context);
    }
    public String getLanguge(){
        return lang;
    }
    public void setLanguage(String language){
        this.lang = language;
    }
}
