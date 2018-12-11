package com.example.tongan.myapplication.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tongan.myapplication.Fragments.HomeFragment;
import com.example.tongan.myapplication.Fragments.ProfileFragment;
import com.example.tongan.myapplication.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation_menu);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.bottom_navigation_home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.bottom_navigation_add:
                    Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                    startActivity(intent);
                    break;

                case R.id.bottom_navigation_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            if (selectedFragment == null) {
                return false;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, selectedFragment).commit();
            return true;
        }
    };

}
