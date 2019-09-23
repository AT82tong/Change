package com.example.tongan.myapplication.Activities;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tongan.myapplication.Adapters.HorizontalDocumentationsRecyclerViewAdapter;
import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.Classes.RequestService;
import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.Helper.DecimalDigitsInputFilter;
import com.example.tongan.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

// NOT GOING TO IMPLEMENT FOR NOW
public class AddPostActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int RequestPermissionCode = 1;
    private static final String TAG = "AddPostActivity";
    protected GoogleApiClient googleApiClient;
    // user location
    double longitude;
    double latitude;
    Geocoder geocoder;
    List<Address> addresses;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    // layouts
    private EditText serviceTitleText;
    private EditText servicePriceText;
    private EditText serviceDescriptionText;
    private EditText serviceStreetAddressText;
    private Spinner serviceCategorySpinner;
    private FusedLocationProviderClient fusedLocationProviderClient;
    // editable inputbox
    private TextInputLayout serviceTitleTextLayout;
    private TextInputLayout servicePriceTextLayout;
    private TextInputLayout serviceDescriptionTextLayout;
    private TextInputLayout serviceAddressTextLayout;

    // clickable buttons
    private ImageView backBtn;
    private Button submitBtn;
    //private Button requestBtn;
    private Button addImageBtn;

    // local variables
    private String serviceTitle;
    private String servicePrice;
    private String serviceDescription;
    private String serviceAddress;
    private String serviceCategory;

    private String randomID;
    private ArrayList<String> serviceImagesStringAL = new ArrayList<>();
    private ArrayList<Uri> serviceImagesUriAL = new ArrayList<>();
    private DocumentReference ref;

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        // Initialize Places
        // Places.initialize(getApplicationContext(), apiK)

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
        servicePriceText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
        serviceDescriptionText = findViewById(R.id.service_description_input);
        serviceStreetAddressText = findViewById(R.id.service_street_address_input);
        serviceCategorySpinner = findViewById(R.id.service_category_spinner);
        backBtn = findViewById(R.id.backBtn);
        submitBtn = findViewById(R.id.submitBtn);
        //requestBtn = findViewById(R.id.requestBtn);
        addImageBtn = findViewById(R.id.addImageBtn);

        serviceTitleTextLayout = findViewById(R.id.service_title_inputLayout);
        servicePriceTextLayout = findViewById(R.id.service_price_inputLayout);
        serviceDescriptionTextLayout = findViewById(R.id.service_description_inputLayout);
        serviceAddressTextLayout = findViewById(R.id.service_address_inputLayout);

        ref = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
        //final DocumentReference ref = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());

        // address drawable right click
        serviceStreetAddressText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (serviceStreetAddressText.getRight() - serviceStreetAddressText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        googleApiClient.connect();
                        //Toast.makeText(AddPostActivity.this, "HERE.",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });

        randomID = UUID.randomUUID().toString();

        //loadProfileInfoFromDatabase();

//        submitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (validateInputs()) {
//                    addPostServiceToDatabase(databaseHelper.getCurrentUserEmail(), serviceTitle, Double.valueOf(servicePrice), "Test Category", serviceDescription, address);
//                    if (!serviceImagesUriAL.isEmpty()) {
//                        savePhotoToDatabase(serviceImagesUriAL);
//                    }
//                }
//            }
//        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    addRequestServiceToDatabase(databaseHelper.getCurrentUserEmail(), serviceTitle, Double.valueOf(servicePrice), "Test Category", serviceDescription, address);
                    if (!serviceImagesUriAL.isEmpty()) {
                        savePhotoToDatabase(serviceImagesUriAL);
                    }
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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

//    private void loadProfileInfoFromDatabase() {
//        documentReference = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
//                if (documentSnapshot != null) {
//                    Map<String, Object> map = documentSnapshot.getData();
//                    userDisplayName = map.get("displayName").toString();
//
//                    user = new User(userDisplayName, databaseHelper.getCurrentUserEmail());
//                }
//            }
//        });
//    }


    // add post service to database
//    public void addPostServiceToDatabase(final String publisherEmail, String serviceTitle, double servicePrice, String category, String serviceDescription, String serviceAddress) {
//        date = new Date();
//        PostService postService = new PostService(randomID, publisherEmail, serviceTitle, servicePrice, category, serviceDescription, serviceAddress, dateFormat.format(date), null, false, null, null);
//        // add post service information to PostService database
//        // get the randomID and update postNumbers in User database
//        firebaseFirestore.collection("PostServices").document(randomID)
//                .set(postService)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        ref.update("postNumbers", FieldValue.arrayUnion(randomID));
//
//                        //postNumbers.add(randomID);
//                        //ref.update("postNumbers", postNumbers);
//                        Toast.makeText(AddPostActivity.this, "Post Service Successful.", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "Post Service Successful.");
//                        Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
//                        startActivity(intent);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Error saving Post Service to database", e);
//                    }
//                });
//    }

    // add request service to database
    public void addRequestServiceToDatabase(final String publisherEmail, String serviceTitle, double servicePrice, String category, String serviceDescription, String serviceAddress) {
        date = new Date();
        RequestService requestService = new RequestService(randomID, publisherEmail, serviceTitle, servicePrice, category, serviceDescription, serviceAddress, dateFormat.format(date), null, false, null, null);
        firebaseFirestore.collection("RequestServices").document(randomID)
                .set(requestService)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ref.update("requestNumbers", FieldValue.arrayUnion(randomID));
                        //requestNumbers.add(randomID);
                        //ref.update("requestNumbers", requestNumbers);
                        Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error saving Request Service to database", e);
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

    // Google location
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


    public void takePhoto() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    public void pickPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            serviceImagesUriAL.add(selectedImage);
            serviceImagesStringAL.add(selectedImage.toString());
            //savePhotoToDatabase(resultCode, selectedImage);
        }

        initRecyclerView(serviceImagesStringAL);

//        switch (requestCode) {
//            case 0:
//                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    savePhotoToDatabase(resultCode, selectedImage);
//                }
//                break;
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    savePhotoToDatabase(resultCode, selectedImage);
//                }
//                break;
//        }
    }

    private void selectImage() {
        final String[] photoOptions = {"Take Photo", "Choose From Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image");
        builder.setItems(photoOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takePhoto();
                        break;
                    case 1:
                        pickPhoto();
                        break;
                }
            }
        });
        builder.show();
    }

    private void initRecyclerView(ArrayList<String> images) {
        RecyclerView recyclerView = findViewById(R.id.postServiceImages);
        HorizontalDocumentationsRecyclerViewAdapter adapter = new HorizontalDocumentationsRecyclerViewAdapter(this, images);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddPostActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    // extension
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void savePhotoToDatabase(ArrayList<Uri> serviceImagesUriAL) {

        final ArrayList<String> imageAL = new ArrayList<>();
        for (final Uri image : serviceImagesUriAL) {
            // store images to storage
            final StorageReference ref = storageReference.child("PostService/" + randomID + "/" + UUID.randomUUID().toString() + "." + getFileExtension(image));
            // store images to firebase
            ref.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final DocumentReference doc = FirebaseFirestore.getInstance().collection("PostServices").document(randomID);
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageAL.add(uri.toString());
                            doc.update("images", imageAL);
                        }
                    });
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
