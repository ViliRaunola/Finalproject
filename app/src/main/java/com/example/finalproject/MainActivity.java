package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    UniversityFragment universityFragment = new UniversityFragment();
    OwnReviewsFragment ownReviewsFragment = new OwnReviewsFragment();
    AccountFragment accountFragment = new AccountFragment();
    EditAccountInformationFragment editAccountInformationFragment = new EditAccountInformationFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Part of onNavigationItemSelected.
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigatio_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Here can be defined what page opens first. If statement is for rotating the screen.
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).commit();
            navigationView.setCheckedItem(R.id.nav_restaurants);
        }

    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            this.moveTaskToBack(true);
        }
    }

    //This handles what item has been selected by Vili.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_restaurants:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).commit();
                Toast.makeText(this, "All Unis", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_reviews:
                sendUniversityFragmentRestaurants();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ownReviewsFragment).commit();
                Toast.makeText(this, "Your reviews", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, accountFragment).commit();
                Toast.makeText(this, "Your Account", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                Toast.makeText(this, "You Have Logged Out", Toast.LENGTH_SHORT).show();
                break;
        }
        //After the item has been selected closes the side menu.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendUniversityFragmentRestaurants(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("key2",universityFragment.universities);
        ownReviewsFragment.setArguments(bundle);
    }
    public void sendUniversityFragmentUniversities(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("key",universityFragment.universities);
        editAccountInformationFragment.setArguments(bundle);
    }


}
