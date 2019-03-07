package com.example.tongan.myapplication.Activities.SettingsPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tongan.myapplication.R;
import com.example.tongan.myapplication.profile_setProfilePhoto;
import com.example.tongan.myapplication.profile_setPublicName;

public class AccountSettingsActivity extends AppCompatActivity {

    RelativeLayout profilePhoto;
    TextView publicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        profilePhoto = findViewById(R.id.profile_photo);
        publicName = findViewById(R.id.public_name);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilePhoto();
            }
        });

        publicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPublicName();
            }
        });

    }

    public void openPublicName() {
        Intent intent = new Intent(this, profile_setPublicName.class);
        startActivity(intent);
    }

    public void openProfilePhoto() {
        Intent intent = new Intent(this, profile_setProfilePhoto.class);
        startActivity(intent);
    }
}
