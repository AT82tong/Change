package com.example.tongan.myapplication.Activities;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tongan.myapplication.Classes.RequestService;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.Helper.DecimalDigitsInputFilter;
import com.example.tongan.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddPostActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "AddPostActivity";

    public static final int RequestPermissionCode = 1;
    protected GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // user location
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
    EditText serviceStreetAddressText;
    Spinner serviceCategorySpinner;

    private TextInputLayout serviceTitleTextLayout;
    private TextInputLayout servicePriceTextLayout;
    private TextInputLayout serviceDescriptionTextLayout;
    private TextInputLayout serviceAddressTextLayout;

    private ImageView backBtn;
    private Button submitButton;

    private String serviceTitle;
    private String servicePrice;
    private String serviceDescription;
    private String serviceAddress;
    private String serviceCategory;

    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private String randomID;
    private ArrayList<String> postNumbers;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        final DatabaseHelper databaseHelper = new DatabaseHelper();

        // location
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        serviceTitleText = findViewById(R.id.service_title_input);
        servicePriceText = findViewById(R.id.service_price_input);
        servicePriceText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});
        serviceDescriptionText = findViewById(R.id.service_description_input);
        serviceStreetAddressText = findViewById(R.id.service_street_address_input);
        serviceCategorySpinner = findViewById(R.id.service_category_spinner);
        backBtn = findViewById(R.id.post_service_back_button);
        submitButton = findViewById(R.id.post_service_submit_btn);

        serviceTitleTextLayout = findViewById(R.id.service_title_inputLayout);
        servicePriceTextLayout = findViewById(R.id.service_price_inputLayout);
        serviceDescriptionTextLayout = findViewById(R.id.service_description_inputLayout);
        serviceAddressTextLayout = findViewById(R.id.service_address_inputLayout);


        // get all postNumbers from current user
        postNumbers = new ArrayList<>();
        documentReference = FirebaseFirestore.getInstance().collection("Users").document(databaseHelper.getCurrentUserEmail());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    if (null != map.get("postNumbers")) {
                        for (String postNumber : (ArrayList<String>) map.get("postNumbers")) {
                            postNumbers.add(postNumber);
                        }
                    }
                }
            }
        });

        // address drawable right click
        serviceStreetAddressText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (serviceStreetAddressText.getRight() - serviceStreetAddressText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        googleApiClient.connect();
                        //Toast.makeText(AddPostActivity.this, "HERE.",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    addPostServiceToDatabase(databaseHelper.getCurrentUserEmail(), serviceTitle, Double.valueOf(servicePrice), "Test Category", serviceDescription, address);
                    //Toast.makeText(AddPostActivity.this, "successful.",Toast.LENGTH_SHORT).show();
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

        serviceTitle = serviceTitleText.getText().toString();
        servicePrice = servicePriceText.getText().toString();
        serviceDescription = serviceDescriptionText.getText().toString();
        serviceAddress = serviceStreetAddressText.getText().toString();


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
        Log.d(TAG, "address: " + address);

        return isValidated;
    }

    // add to database
    public void addPostServiceToDatabase(final String publisherEmail, String serviceTitle, double servicePrice, String category, String serviceDescription, String serviceAddress ) {
        randomID = UUID.randomUUID().toString();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        PostService postService = new PostService(publisherEmail, serviceTitle, servicePrice, category, serviceDescription, serviceAddress, dateFormat.format(date), null);
        // add post service information to PostService database
        // get the randomID and update postNumbers in User database
        firebaseFirestore.collection("PostServices").document(randomID)
                .set(postService)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        postNumbers.add(randomID);
                        documentReference.update("postNumbers", postNumbers);
                        Toast.makeText(AddPostActivity.this, "Post Service Successful.",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Post Service Successful.");
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error saving to database", e);
                    }
                });


        // add info to RequestService database
        firebaseFirestore.collection("RequestService").document(randomID)
                .set(postService)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        postNumbers.add(randomID);
                        documentReference.update("requestNumbers", postNumbers);
                        Toast.makeText(AddPostActivity.this, "Post Service Successful.",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Post Service Successful.");
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error saving to database", e);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //googleApiClient.connect();
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
                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                address = addresses.get(0).getAddressLine(0);
                                city = addresses.get(0).getLocality();
                                state = addresses.get(0).getAdminArea();
                                country = addresses.get(0).getCountryName();
                                postalCode = addresses.get(0).getPostalCode();
                                knownName = addresses.get(0).getFeatureName();

//                                Log.d(TAG, "address: " + addresses.get(0).getThoroughfare());
                                Toast.makeText(AddPostActivity.this, "" + addresses.get(0).getThoroughfare(), Toast.LENGTH_SHORT).show();

//                                Log.d(TAG, "city: " + city);
//                                Log.d(TAG, "state: " + state);
//                                Log.d(TAG, "country: " + country);
//                                Log.d(TAG, "postalCode: " + postalCode);
//                                Log.d(TAG, "knownName: " + knownName);

                                if (!country.equals("United States")) {
                                    serviceAddressTextLayout.setError("Currently supporting United State only,");
                                } else {
                                    serviceStreetAddressText.setText(address);
                                    serviceAddressTextLayout.setErrorEnabled(false);
                                }
                            } else {
                                serviceAddressTextLayout.setError("Unable to locate");
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
        Log.d(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspendedd");
    }
}
