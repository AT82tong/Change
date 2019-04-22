package com.example.tongan.myapplication.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tongan.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddPostActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "AddPostActivity";

    public static final int RequestPermissionCode = 1;
    protected GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;

    // user location
    LocationManager locationManager;
    Location location;
    double longitude;
    double latitude;

    Geocoder geocoder;
    List<Address> addresses;
    String address;
    String city;
    String state;
    String country;
    String postalCode;
    String knownName;

    // layouts
    EditText serviceTitleText;
    EditText servicePriceText;
    EditText serviceDescriptionText;
    EditText serviceAddressText;
    Spinner serviceCategorySpinner;

    private TextInputLayout serviceTitleTextLayout;
    private TextInputLayout servicePriceTextLayout;
    private TextInputLayout serviceDescriptionTextLayout;
    private TextInputLayout serviceAddressTextLayout;


    private ImageView backBtn;
    private Button submitButton;

    private String serviceTitle;
    private String servicePrice;
    private StringBuilder serviceDescription;
    private String serviceAddress;

    private StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        serviceTitleText = findViewById(R.id.service_title_input);
        servicePriceText = findViewById(R.id.service_price_input);
        serviceDescriptionText = findViewById(R.id.service_description_input);
        serviceAddressText = findViewById(R.id.service_address_input);
        serviceCategorySpinner = findViewById(R.id.service_category_spinner);
        backBtn = findViewById(R.id.post_service_back_button);
        submitButton = findViewById(R.id.post_service_submit_btn);

        serviceTitleTextLayout = findViewById(R.id.service_title_inputLayout);
        servicePriceTextLayout = findViewById(R.id.service_price_inputLayout);
        serviceDescriptionTextLayout = findViewById(R.id.service_description_inputLayout);
        serviceAddressTextLayout = findViewById(R.id.service_address_inputLayout);


        serviceTitle = serviceTitleText.getText().toString();
        servicePrice = servicePriceText.getText().toString();
        serviceDescription = sb.append(serviceDescriptionText.getText().toString());
        serviceAddress = serviceAddressText.getText().toString();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    Toast.makeText(AddPostActivity.this, "fail.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn();
    }

    public void backBtn() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public boolean validateInputs() {

        boolean isValidated = true;
        if (TextUtils.isEmpty(serviceTitle)) {
            serviceTitleTextLayout.setError("Service title cannot be empty");
            isValidated = false;
        } else {
            serviceAddressTextLayout.setErrorEnabled(false);
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



    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d(TAG, "longitude: " + longitude);
                                Log.d(TAG, "latitude: " + latitude);
                            }
                        }
                    });
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(AddPostActivity.this, new
                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection suspendedd");
    }
}
