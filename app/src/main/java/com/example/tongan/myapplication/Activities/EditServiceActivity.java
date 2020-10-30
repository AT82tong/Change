package com.example.tongan.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tongan.myapplication.Classes.Service;
import com.example.tongan.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.DecimalFormat;

public class EditServiceActivity extends AppCompatActivity implements Serializable {

    private static final String TAG = "EditActivity";

    private EditText serviceTitleText;
    private EditText servicePriceText;
    private EditText serviceDescriptionText;
    private EditText serviceAddressText;
    private EditText maxAcceptors;
    private Spinner serviceCategorySpinner;
    private FusedLocationProviderClient fusedLocationProviderClient;
    // editable inputbox
    private TextInputLayout serviceTitleTextLayout;
    private TextInputLayout servicePriceTextLayout;
    private TextInputLayout serviceDescriptionTextLayout;
    private TextInputLayout serviceAddressTextLayout;

    private CheckBox maxAcceptorsCheckBox;

    private String serviceTitle;
    private String servicePrice;
    private String serviceDescription;
    private String serviceAddress;

    private Button submitBtn;
    private int maxAcceptorsNumber;

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

        maxAcceptors = findViewById(R.id.maxAcceptors);
        maxAcceptorsCheckBox = findViewById(R.id.maxAcceptorsNoLimit);
        submitBtn = findViewById(R.id.submitBtn);


        final Service service;
        if (getIntent().getSerializableExtra("postService") != null) {
            service = (Service) getIntent().getSerializableExtra("postService");
        } else {
            service = (Service) getIntent().getSerializableExtra("requestService");
        }

        final String serviceType = getIntent().getStringExtra("serviceType");
        DecimalFormat value = new DecimalFormat("0.00");

        serviceTitleText.setText(service.getServiceTitle());
        servicePriceText.setText(value.format(service.getPrice()));
        serviceDescriptionText.setText(service.getDescription());
        serviceAddressText.setText(service.getAddress());

        if (service.getMaxAcceptor() == -1) {
            maxAcceptorsCheckBox.setChecked(true);
            maxAcceptors.setText("No Limit");
            maxAcceptors.setEnabled(false);
            maxAcceptors.setClickable(false);
        } else {
            maxAcceptorsCheckBox.setChecked(false);
            maxAcceptors.setText("" + service.getMaxAcceptor());
            maxAcceptors.setEnabled(true);
            maxAcceptors.setClickable(true);
        }

        maxAcceptorsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    maxAcceptorsNumber = -1;
                    maxAcceptors.setText("No Limit");
                    maxAcceptors.setEnabled(false);
                    maxAcceptors.setClickable(false);
                } else {
                    maxAcceptorsNumber = 0;
                    maxAcceptors.setText("");
                    maxAcceptors.setEnabled(true);
                    maxAcceptors.setClickable(true);
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    updateServiceInDatabase(serviceType, service.getId(), serviceTitle, Double.valueOf(servicePrice), "Test Category", serviceDescription, serviceAddress, maxAcceptorsNumber);
                    onBackPressed();
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

        if (maxAcceptorsNumber != -1) {
            if (TextUtils.isEmpty(maxAcceptors.getText().toString()) || Integer.parseInt(maxAcceptors.getText().toString()) == 0) {
                Toast.makeText(EditServiceActivity.this, "maxAcceptors error.", Toast.LENGTH_LONG).show();
                isValidated = false;
            } else {
                maxAcceptorsNumber = Integer.parseInt(maxAcceptors.getText().toString());
            }
        }

        return isValidated;
    }

    // IMAGE NOT DONE, NEED TO COMPLETE IN FUTURE
    public void updateServiceInDatabase(String serviceType, String serviceId, String serviceTitle, double servicePrice, String serviceCategory, String serviceDescription, String serviceAddress, int maxAcceptorsNumber) {
        DocumentReference ref = FirebaseFirestore.getInstance().collection(serviceType).document(serviceId);
        ref.update("serviceTitle", serviceTitle);
        ref.update("price", servicePrice);
        ref.update("category", serviceCategory);
        ref.update("description", serviceDescription);
        ref.update("address", serviceAddress);
        ref.update("maxAcceptor", maxAcceptorsNumber);
    }
}
