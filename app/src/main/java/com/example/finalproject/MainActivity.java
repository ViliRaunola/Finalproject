package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    UniversityFragment universityFragment = new UniversityFragment();
    OwnReviewsFragment ownReviewsFragment = new OwnReviewsFragment();
    AccountFragment accountFragment = new AccountFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    private Toolbar toolbar;
    ParseClass parseClass = ParseClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Language.getInstance().loadLocale(this);
        System.out.println(Locale.getDefault().getLanguage());



        //set the toolbar text
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));

        //Part of onNavigationItemSelected.
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Here can be defined what page opens first. If statement is for rotating the screen.
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).commit();
            navigationView.setCheckedItem(R.id.nav_restaurants);
        }

        //https://stackoverflow.com/questions/34973456/how-to-change-text-of-a-textview-in-navigation-drawer-header first answer

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userHeader);
        navUsername.setText(User.getInstance().getFirstName() + " " + User.getInstance().getLastName());
    }

    @Override
    public void onBackPressed() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.moveTaskToBack(true);
        } else if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();

        }else{

        }
    }

    //This handles what item has been selected by Vili.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_restaurants:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).addToBackStack("restaurant_fragment").commit();
                Toast.makeText(this, "All Unis", Toast.LENGTH_SHORT).show();
                toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));
                break;
            case R.id.nav_reviews:
                //sendUniversityFragmentRestaurantsOwnReviews();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ownReviewsFragment).addToBackStack("reviews_fragment").commit();
                Toast.makeText(this, "Your reviews", Toast.LENGTH_SHORT).show();
                toolbar.setTitle(getResources().getString(R.string.sideMenu_ownReviews));
                break;
            case R.id.nav_account:
                //sendUniversityFragmentRestaurantsAccount();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, accountFragment).addToBackStack("account_fragment").commit();
                Toast.makeText(this, "Your Account", Toast.LENGTH_SHORT).show();
                toolbar.setTitle(getResources().getString(R.string.sideMenu_account));
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, settingsFragment).addToBackStack("settings_fragment").commit();
                toolbar.setTitle(getResources().getString(R.string.sideMenu_settings));
                break;
            case R.id.nav_logout:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                parseClass.getUniversities().clear();
                parseClass.getRestaurants().clear();
                parseClass.getAllReviews().clear();
                parseClass.getReviewsNotPublished().clear();
                parseClass.getReviewsPublished().clear();

                finish();
                Toast.makeText(this, "You Have Logged Out", Toast.LENGTH_SHORT).show();
                break;
        }
        //After the item has been selected closes the side menu.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*
    public void sendUniversityFragmentRestaurantsOwnReviews(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("key2",universityFragment.universities);
        ownReviewsFragment.setArguments(bundle);

    }

 */
/*
    public void sendUniversityFragmentRestaurantsAccount(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("key2",universityFragment.universities);
        accountFragment.setArguments(bundle);
    }

 */

  /* @SuppressWarnings("deprecation")
    public void setLocale(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_language", language);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("My_language","");
        setLocale(language);
    }*/

}
