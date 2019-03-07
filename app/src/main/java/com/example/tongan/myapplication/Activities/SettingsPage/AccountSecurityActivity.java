package com.example.tongan.myapplication.Activities.SettingsPage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tongan.myapplication.R;

public class AccountSecurityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}
