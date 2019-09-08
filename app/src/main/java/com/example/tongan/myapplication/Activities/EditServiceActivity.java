package com.example.tongan.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Helper.DecimalDigitsInputFilter;
import com.example.tongan.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

public class EditServiceActivity extends AppCompatActivity implements Serializable {

    private static final String TAG = "EditActivity";

    private EditText serviceTitleText;
    private EditText servicePriceText;
    private EditText serviceDescriptionText;
    private EditText serviceAddressText;
    private Spinner serviceCategorySpinner;
    private FusedLocationProviderClient fusedLocationProviderClient;
    // editable inputbox
    private TextInputLayout serviceTitleTextLayout;
    private TextInputLayout servicePriceTextLayout;
    private TextInputLayout serviceDescriptionTextLayout;
    private TextInputLayout serviceAddressTextLayout;

    private String serviceTitle;
    private String servicePrice;
    private String serviceDescription;
    private String serviceAddress;

    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        serviceTitleText = findViewById(R.id.serviceTitle);
        servicePriceText = findViewById(R.id.servicePrice);
        serviceDescriptionText = findViewById(R.id.serviceDescription);
        serviceAddressText = findViewById(R.id.serviceAddress);
        serviceCategorySpinner = findViewById(R.id.serviceCategory);

        serviceTitleTextLayout = findViewById(R.id.service_title_inputLayout);
        servicePriceTextLayout = findViewById(R.id.service_price_inputLayout);
        serviceDescriptionTextLayout = findViewById(R.id.service_description_inputLayout);
        serviceAddressTextLayout = findViewById(R.id.service_address_inputLayout);

        submitBtn = findViewById(R.id.submitBtn);

        final PostService postService = (PostService) getIntent().getSerializableExtra("postService");
        final String serviceType = getIntent().getStringExtra("serviceType");
        DecimalFormat value = new DecimalFormat("0.00");

        serviceTitleText.setText(postService.getServiceTitle());
        servicePriceText.setText(value.format(postService.getPrice()));
        serviceDescriptionText.setText(postService.getDescription());
        serviceAddressText.setText(postService.getAddress());

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    addServiceToDatabase(serviceType, postService.getId(), serviceTitle, Double.valueOf(servicePrice), "Test Category", serviceDescription, serviceAddress);
                }
            }
        });
    }

    public boolean validateInputs() {

        serviceTitle = serviceTitleText.getText().toString();
        servicePrice = servicePriceText.getText().toString();
        serviceDescription = serviceDescriptionText.getText().toString();
        serviceAddress = serviceAddressText.getText().toString();

        boolean isValidated = true;
        if (TextUtils.isEmpty(serviceTitle)) {
            serviceTitleTextLayout.setError("Service title cannot be empty");
            isValidated = false;
        } else {
            serviceTitleTextLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(servicePrice)) {
            servicePriceTextLayout.setError("Service price cannot be empty");
            isValidated = false;
        } else {
            servicePriceTextLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(serviceDescription)) {
            serviceDescriptionTextLayout.setError("Service description cannot be empty");
            isValidated = false;
        } else {
            serviceDescriptionTextLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(serviceAddress)) {
            serviceAddressTextLayout.setError("Address cannot be empty");
            isValidated = false;
        } else {
            serviceAddressTextLayout.setErrorEnabled(false);
        }

        return isValidated;
    }

    // IMAGE NOT DONE, NEED TO COMPLETE IN FUTURE
    public void addServiceToDatabase(String serviceType, String serviceId, String serviceTitle, double servicePrice, String serviceCategory, String serviceDescription, String serviceAddress) {
        DocumentReference ref = FirebaseFirestore.getInstance().collection(serviceType).document(serviceId);
        ref.update("serviceTitle", serviceTitle);
        ref.update("price", servicePrice);
        ref.update("category", serviceCategory);
        ref.update("description", serviceDescription);
        ref.update("address", serviceAddress);
    }
}
