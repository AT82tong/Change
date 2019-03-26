package com.example.tongan.myapplication.Activities.SettingsPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.Activities.LoginActivity;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    Button signOutBtn;

    ImageView backBtn;
    CircleImageView profileImage;

    RelativeLayout settingsBtn;

    TextView securityBtn;
    TextView notificationsBtn;
    TextView privacyBtn;
    TextView generalBtn;
    TextView aboutBtn;
    TextView profileName;

    private DocumentReference documentReference;
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

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
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);

        documentReference = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    profileName.setText(map.get("displayName").toString());
                    if (map.get("profileImage") == null) {
                        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_profile_picture));
                    } else {
                        Glide.with(getApplicationContext()).load(map.get("profileImage").toString()).into(profileImage);
                    }
                }
            }
        });

//        // display profileName and profileImage
//        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                                if (queryDocumentSnapshot.getId().equals(databaseHelper.getCurrentUserEmail())) {
//                                    Map<String, Object> map = queryDocumentSnapshot.getData();
//                                    profileName.setText(map.get("displayName").toString());
//                                    if (map.get("profileImage") == null) {
//                                        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_profile_picture));
//                                    } else {
//                                        Glide.with(ProfileSettingsActivity.this).load(map.get("profileImage").toString()).into(profileImage);
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                });
//            }
//        });

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
