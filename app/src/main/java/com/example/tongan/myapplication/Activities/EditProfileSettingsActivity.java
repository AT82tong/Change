package com.example.tongan.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tongan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class EditProfileSettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        signOutBtn = findViewById(R.id.sign_out_button);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
            }
        });
    }
}
