package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private UniversityFragment universityFragment = new UniversityFragment();
    private OwnReviewsFragment ownReviewsFragment = new OwnReviewsFragment();
    private AccountFragment accountFragment = new AccountFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();
    private ParseClass parseClass = ParseClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the current language
        Language.getInstance().loadLocale(this);

        //set the toolbar text
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));

        //Part of onNavigationItemSelected.
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //for opening and closing navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Here can be defined what page opens first. If statement is for rotating the screen.
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).commit();
            navigationView.setCheckedItem(R.id.nav_restaurants);
        }

        //https://stackoverflow.com/questions/34973456/how-to-change-text-of-a-textview-in-navigation-drawer-header first answer
        //setting drawer header for the current user
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userHeader);
        navUsername.setText(User.getInstance().getFirstName() + " " + User.getInstance().getLastName());
    }

    @Override
    public void onBackPressed() {

        //if drawer is open --> close the drawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        //if Restaurant Menus page is open --> move task to back
        }else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof UniversityFragment){
            this.moveTaskToBack(true);

        //if one of side menu pages is open --> Restaurant Menus page
        }else if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof AccountFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).commit();
            toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));
        }else if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof OwnReviewsFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).commit();
            toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));
        }else if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SettingsFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).commit();
            toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));

        //if edit review page is open --> Own reviews page
        }else if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EditReviewsFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ownReviewsFragment).commit();
            toolbar.setTitle(getResources().getString(R.string.sideMenu_ownReviews));

        //if edit account information is open --> Account page
        }else if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EditAccountInformationFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, accountFragment).commit();
            toolbar.setTitle(getResources().getString(R.string.sideMenu_account));

        //if food reviews page is open --> Restaurant Menus page
        }else if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof FoodReviewsFragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).commit();
            toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));

        //if add new review page is open --> View reviews page
        }else if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof AddNewReviewFragment){
            super.onBackPressed();
            toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));

        //if view reviews page is open --> Restaurant Menus page
        }else if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ViewReviewFragment){
            super.onBackPressed();
            toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));

        }else {
            super.onBackPressed();
        }

    }

    //This handles what item has been selected and switching fragment/activity and setting the toolbar as current page
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_restaurants:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, universityFragment).commit();
                toolbar.setTitle(getResources().getString(R.string.sideMenu_restaurantMenus));
                break;

            case R.id.nav_reviews:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ownReviewsFragment).commit();
                toolbar.setTitle(getResources().getString(R.string.sideMenu_ownReviews));
                break;

            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, accountFragment).commit();
                toolbar.setTitle(getResources().getString(R.string.sideMenu_account));
                break;

            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, settingsFragment).commit();
                toolbar.setTitle(getResources().getString(R.string.sideMenu_settings));
                break;

            case R.id.nav_logout:
                //clearing all the lists
                parseClass.getUniversities().clear();
                parseClass.getRestaurants().clear();
                parseClass.getAllReviews().clear();
                parseClass.getReviewsNotPublished().clear();
                parseClass.getReviewsPublished().clear();

                //finishing the activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

                Toast.makeText(this, getResources().getString(R.string.toast_loggedOut), Toast.LENGTH_SHORT).show();
                break;
        }
        //After the item has been selected closes the side menu.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
