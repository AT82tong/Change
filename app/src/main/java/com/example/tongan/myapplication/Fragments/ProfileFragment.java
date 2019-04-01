package com.example.tongan.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tongan.myapplication.Activities.SettingsPage.ProfileSettingsActivity;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private DocumentReference documentReference;

    ImageView settingsImage;
    CircleImageView profileImage;

    TextView profileName;
    TextView profileFollowers;
    TextView profileRating;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        settingsImage = view.findViewById(R.id.edit_profile_settings);
        profileImage = view.findViewById(R.id.profile_image);

        profileName = view.findViewById(R.id.profile_name);
        profileFollowers = view.findViewById(R.id.profile_followers);
        profileRating = view.findViewById(R.id.profile_rating);

        // update data
        documentReference = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                    String followers = numberFormat.format(map.get("follower"));
                    DecimalFormat value = new DecimalFormat("0.0");

                    profileName.setText(map.get("displayName").toString());
                    profileFollowers.setText(followers);
                    profileRating.setText(value.format(map.get("rating")));

                    if (map.get("profileImage") == null) {
                        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_profile_picture));
                    } else {
                        Glide.with(ProfileFragment.this).load(map.get("profileImage").toString()).into(profileImage);
                    }
                }
            }
        });

        settingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null) {
            return;
        }
        Log.d(TAG, "ProfileFragment Page");
    }


//        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                        if (queryDocumentSnapshot.getId().equals(databaseHelper.getCurrentUserEmail())) {
//
//                            Map<String, Object> map = queryDocumentSnapshot.getData();
//                            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
//                            String followers = numberFormat.format(map.get("follower"));
//                            profileName.setText(map.get("displayName").toString());
//                            profileFollowers.setText(followers);
//
//                            DecimalFormat value = new DecimalFormat("0.0");
//                            profileRating.setText(value.format(map.get("rating")));
//                        }
//                    }
//                }
//            }
//        });

}
