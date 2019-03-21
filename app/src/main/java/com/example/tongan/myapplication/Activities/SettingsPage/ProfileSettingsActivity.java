package com.example.tongan.myapplication.Activities.SettingsPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tongan.myapplication.Activities.LoginActivity;
import com.example.tongan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileSettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    Button signOutBtn;

    ImageView backBtn;

    RelativeLayout settingsBtn;

    TextView securityBtn;
    TextView notificationsBtn;
    TextView privacyBtn;
    TextView generalBtn;
    TextView aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        signOutBtn = findViewById(R.id.sign_out_button);
        backBtn = findViewById(R.id.settings_back_button);
        settingsBtn = findViewById(R.id.account_settings);
        securityBtn = findViewById(R.id.account_security);
        notificationsBtn = findViewById(R.id.account_notifications);
        privacyBtn = findViewById(R.id.account_privacy);
        generalBtn = findViewById(R.id.account_general);
        aboutBtn = findViewById(R.id.account_about);


        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileSettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });


        securityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, AccountSecurityActivity.class);
                startActivity(intent);
            }
        });


        privacyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, AccountPrivacyActivity.class);
                startActivity(intent);
            }
        });


        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, AccountNotificationsActivity.class);
                startActivity(intent);
            }
        });


        generalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, AccountGeneralActivity.class);
                startActivity(intent);
            }
        });


        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, AccountAboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
