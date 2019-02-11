package com.example.tongan.myapplication.Activities.SettingsPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.tongan.myapplication.R;

public class AccountNotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_notifications);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}
