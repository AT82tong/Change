package com.example.tongan.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile_setPublicName extends AppCompatActivity {

    EditText publicName;
    Button saveButton;
    ImageView backButton;


    private DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set_public_name);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        publicName = findViewById(R.id.profile_set_name);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.name_back_button);

        publicName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(publicName, InputMethodManager.SHOW_IMPLICIT);

        publicName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(publicName.getText().toString().length() > 0);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(databaseHelper.getCurrentUserEmail());
                doc.update("displayName", publicName.getText().toString());
                onBackPressed();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
