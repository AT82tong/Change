package com.example.tongan.myapplication.Activities.SettingsPage.MyProfile;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tongan.myapplication.Adapters.VerticalDocumentationsRecyclerViewAdapter;
import com.example.tongan.myapplication.Helper.DatabaseHelper;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

public class ManageDocumentations extends AppCompatActivity {

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private DatabaseHelper databaseHelper = new DatabaseHelper();

    ImageView backBtn;
    ImageView addDocumentations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_documentations);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        backBtn = findViewById(R.id.back_button);
        addDocumentations = findViewById(R.id.add_documentations);

        loadPhotosFromDatabase();

        addDocumentations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }


    public void takePhoto() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
        //Toast.makeText(ManageDocumentations.this, "takePhoto()",Toast.LENGTH_SHORT).show();
    }

    public void pickPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
        //Toast.makeText(ManageDocumentations.this, "pickPhoto()",Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    savePhotoToDatabase(resultCode, selectedImage);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    savePhotoToDatabase(resultCode, selectedImage);
                }
                break;
        }
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
        RecyclerView recyclerView = findViewById(R.id.documentations_recycler_view);
        VerticalDocumentationsRecyclerViewAdapter adapter = new VerticalDocumentationsRecyclerViewAdapter(this, images);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // extension
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void savePhotoToDatabase(int resultCode, Uri selectedImage) {
        String formatedEmail = databaseHelper.formatedEmail(databaseHelper.getCurrentUserEmail());

        String photoOption;
        if (resultCode == 0) {
            photoOption = "TakePhoto";
        } else {
            photoOption = "PickPhoto";
        }

        final ArrayList<String> imageAL = new ArrayList<>();

        final StorageReference ref = storageReference.child("Image/" + formatedEmail + "/" + photoOption + UUID.randomUUID().toString() + "." + getFileExtension(selectedImage));
        ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(databaseHelper.getCurrentUserEmail());
                doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {
                            Map<String, Object> map = documentSnapshot.getData();
                            if (map.get("images") != null) {
                                for (String imageUrl : (ArrayList<String>) map.get("images")) {
                                    imageAL.add(imageUrl);
                                }
                            }
                        }
                    }
                });
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageAL.add(uri.toString());
                        doc.update("images", imageAL);
                        Toast.makeText(ManageDocumentations.this, "Document Upload Successful.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadPhotosFromDatabase() {
        DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(databaseHelper.getCurrentUserEmail());
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Map<String, Object> map = documentSnapshot.getData();
                    if (map.get("images") != null) {
                        ArrayList<String> images = new ArrayList<>();
                        images.addAll((ArrayList<String>) map.get("images"));
                        initRecyclerView(images);
                    }
                }
            }
        });
    }
}