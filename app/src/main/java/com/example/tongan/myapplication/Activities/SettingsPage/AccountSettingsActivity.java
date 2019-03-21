package com.example.tongan.myapplication.Activities.SettingsPage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.example.tongan.myapplication.profile_setProfilePhoto;
import com.example.tongan.myapplication.profile_setPublicName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingsActivity";

    RelativeLayout profilePhoto;
    TextView publicName;
    ImageView profileImage;
    ImageView backButton;

    private DatabaseReference databaseReference;

    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        profilePhoto = findViewById(R.id.profile_photo);
        publicName = findViewById(R.id.public_name);
        profileImage = findViewById(R.id.settings_profile_image);
        backButton = findViewById(R.id.settings_back_button);

        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        if (queryDocumentSnapshot.getId().equals(databaseHelper.getCurrentUserEmail())) {
                            // need to finish
                            // get the profileImageURL
                            // find in storage and set it
                        }
                    }
                }
            }
        });

        // load profile image
//        Glide.with(this).load(image).placeholder(R.drawable.settings_profile_picture).error(R.drawable.settings_profile_picture).listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                Log.d(TAG, "Load profile image failed.");
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                return false;
//            }
//        }).into(profileImage);


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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);            }
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
