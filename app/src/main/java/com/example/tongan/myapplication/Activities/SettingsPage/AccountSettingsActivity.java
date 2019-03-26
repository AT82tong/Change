package com.example.tongan.myapplication.Activities.SettingsPage;

import android.accounts.AccountsException;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.example.tongan.myapplication.profile_setProfilePhoto;
import com.example.tongan.myapplication.profile_setPublicName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingsActivity";

    private final int PICK_IMAGE_REQUEST = 71;

    RelativeLayout profilePhotoLayout;
    RelativeLayout profileNameLayout;

    TextView publicName;

    CircleImageView profileImage;
    ImageView backButton;

    private Uri filePath;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DocumentReference documentReference;
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        profilePhotoLayout = findViewById(R.id.profile_photo);
        profileNameLayout = findViewById(R.id.profile_name);

        publicName = findViewById(R.id.public_name);
        profileImage = findViewById(R.id.profile_image);
        backButton = findViewById(R.id.settings_back_button);

        // load image into profile image. on data change
        documentReference = firebaseFirestore.collection("Users").document(databaseHelper.getCurrentUserEmail());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    publicName.setText(map.get("displayName").toString());
                    if (map.get("profileImage") == null) {
                        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_profile_picture));
                    } else {
                        Glide.with(getApplicationContext()).load(map.get("profileImage").toString()).into(profileImage);
                    }
                }
            }
        });

        // load image into profile image
//        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                        if (queryDocumentSnapshot.getId().equals(databaseHelper.getCurrentUserEmail())) {
//                            Map<String, Object> map = queryDocumentSnapshot.getData();
//                            if (map.get("profileImage") == null) {
//                                profileImage.setImageDrawable(getResources().getDrawable(R.drawable.settings_profile_picture));
//                            } else {
//                                Glide.with(AccountSettingsActivity.this).load(map.get("profileImage").toString()).into(profileImage);
//                            }
//                            break;
//                        }
//                    }
//                }
//            }
//        });
        // ------

        profilePhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        profileNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPublicName();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void editPublicName() {
        Intent intent = new Intent(this, profile_setPublicName.class);
        startActivity(intent);
    }

    private void choosePhoto() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select Photo"), PICK_IMAGE_REQUEST);
    }

    // extension
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
                savePhoto();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePhoto() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String email = databaseHelper.getCurrentUserEmail().substring(0, databaseHelper.getCurrentUserEmail().indexOf(".com"));
            String formatedEmail = email.replaceAll("\\.", "/");

            // delete original profile image and storage image to `Storage` and save URL to database
            storageReference.child("Image/" + formatedEmail + "/ProfileImage" + "." + getFileExtension(filePath)).delete();
            final StorageReference ref = storageReference.child("Image/" + formatedEmail + "/ProfileImage" + "." + getFileExtension(filePath));
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(AccountSettingsActivity.this, "Successfully Uploaded", Toast.LENGTH_LONG).show();

                    final DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(databaseHelper.getCurrentUserEmail());
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            doc.update("profileImage", uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AccountSettingsActivity.this, "Fail to upload image", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }

}
