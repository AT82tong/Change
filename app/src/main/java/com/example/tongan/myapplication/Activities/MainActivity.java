package com.example.tongan.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.tongan.myapplication.Fragments.HomeFragment;
import com.example.tongan.myapplication.Fragments.ProfileFragment;
import com.example.tongan.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "MainActivity Page");
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
            Intent intent;

            switch (menuItem.getItemId()) {
                case R.id.bottom_navigation_home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.bottom_navigation_add:
                    intent = new Intent(MainActivity.this, AddServiceActivity.class);
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
