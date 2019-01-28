package com.example.tongan.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.tongan.myapplication.R;

public class EditProfileSettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
