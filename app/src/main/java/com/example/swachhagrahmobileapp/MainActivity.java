package com.example.swachhagrahmobileapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.Fragment.HomeFragment;
import com.Fragment.RatingFragment;
import com.Fragment.ProfileFragment;
import com.Fragment.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    Fragment selectedfragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //todo 2.1 activating bottom navigation so that it can accept listener on its buttons..
        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);//goes to tod 2.3 line 59
        //todo 2.2 listener function is set now..

        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("publisherid");
            //we will save the profileid of current loggedin user in PREFS.xml file.. so that for every screen we didn't have to login again.. we will simply access there profile id by getsharedprefference..
            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

    }
//todo 2.3 defining callback onNavigationItemSelected which will be called by listener of tod0 2.2
    /*basics--
     BottomNavigationView is a interface class
     has abstract callback method onNavigationItemSelected- which need to be define here and call by our listener of tod02.2

     //the callback method defination consist of what screen to launch when particular is selected..
     Note- we didn't use intent here to launch screen of need, insted used fragment as a subactivity of main activity
     beacuse main activity has 2 part top part is where fragment launchess..and 2nd part (bottom) has bottom navigation which should be there on screen everytime..
     if intent used then bottom navi will be lost and we have to press back button everytime...
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_search:
                            selectedfragment = new SearchFragment();
                            break;
                        case R.id.nav_home:
                            selectedfragment = new HomeFragment();
                            break;

                        case R.id.nav_add:
                            selectedfragment = null;
                            startActivity(new Intent(MainActivity.this, PostActivity.class));
                            break;
                        case R.id.nav_heart:
                            selectedfragment = new RatingFragment();
                            break;
                        case R.id.nav_profile:
                            //The getSharedPreferences(name, mode) method automatically creates the file with the name specified, so you don't need to create it.
                            //here
                            //PREFS.XML will be created in which <profileid> tag will be created having value as id of current logged in user
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selectedfragment = new ProfileFragment();
                            break;
                    }
                    if (selectedfragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedfragment).commit();
                    }

                    return true;
                }
            };
}