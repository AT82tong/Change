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

import com.example.tongan.myapplication.Activities.EditProfileSettingsActivity;
import com.example.tongan.myapplication.Activities.SignUpActivity;
import com.example.tongan.myapplication.R;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    ImageView settingsImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        settingsImage = view.findViewById(R.id.edit_profile_settings);

        settingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileSettingsActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ProfileFragment Page");
    }
}
