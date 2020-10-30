package com.example.tongan.myapplication.Helper;

import com.example.tongan.myapplication.Classes.Service;
import com.example.tongan.myapplication.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DatabaseHelper {

    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    final User user = new User();


    public String getCurrentUserEmail() {
        return fireBaseAuth.getCurrentUser().getEmail();
    }

    public String formatedEmail(String email) {
        return email.substring(0, getCurrentUserEmail().indexOf(".com")).replaceAll("\\.", "/");
    }

//    public User getUserData (final String email) {
//        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                        if (queryDocumentSnapshot.getId().equals(email)) {
//                            user.setFirstName(queryDocumentSnapshot.getData().get("firstName").toString());
//                            user.setLastName(queryDocumentSnapshot.getData().get("lastName").toString());
//                            user.setEmail(queryDocumentSnapshot.getData().get("email").toString());
//                            user.setPassword(queryDocumentSnapshot.getData().get("password").toString());
//                            break;
//                        }
//                    }
//                    Log.d("DB", "firstName: " + user.getFirstName());
//                    Log.d("DB", "lastName: " + user.getLastName());
//                    Log.d("DB", "email: " + user.getEmail());
//                    Log.d("DB", "password: " + user.getPassword());
//                }
//            }
//        });
//            return user;
//    }
//
//        public void getUserDataDB () {
//        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, "document: " + document.getId() + " -> " + document.getData());
//                        Log.d(TAG, "document: " + document.getId() + " -> " + document.getData().get("firstName"));
//                    }
//                }
//            }
//        });
//
//    }
}
