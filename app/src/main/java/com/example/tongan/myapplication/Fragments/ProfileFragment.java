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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tongan.myapplication.Activities.EditProfileSettingsActivity;
import com.example.tongan.myapplication.Activities.SignUpActivity;
import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private DatabaseHelper db = new DatabaseHelper();

    ImageView settingsImage;
    TextView profileName;
    TextView profileFollowers;
    TextView profileRating;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        settingsImage = view.findViewById(R.id.edit_profile_settings);

        init(view);

        settingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileSettingsActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }

    public void init(View v) {

        profileName = v.findViewById(R.id.profile_name);
        profileFollowers = v.findViewById(R.id.profile_followers);
        profileRating = v.findViewById(R.id.profile_rating);

        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        if (queryDocumentSnapshot.getId().equals(db.getCurrentUserEmail())) {
                            profileName.setText("" + queryDocumentSnapshot.getData().get("firstName") + " " + queryDocumentSnapshot.getData().get("lastName"));
                            profileFollowers.setText("" + queryDocumentSnapshot.getData().get("follower"));
                            profileRating.setText("" + queryDocumentSnapshot.getData().get("rating"));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ProfileFragment Page");
    }
}
