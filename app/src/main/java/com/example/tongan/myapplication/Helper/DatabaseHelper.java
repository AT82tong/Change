package com.example.tongan.myapplication.Helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tongan.myapplication.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DatabaseHelper {

    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public String getCurrentUserEmail () {
        return fireBaseAuth.getCurrentUser().getEmail();
    }

    public User getUserData (final String email) {
        final User user = new User();
        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        if (queryDocumentSnapshot.getId().equals(email)) {
                            user.setFirstName(queryDocumentSnapshot.getData().get("firstName").toString());
                            user.setLastName(queryDocumentSnapshot.getData().get("lastName").toString());
                            user.setEmail(queryDocumentSnapshot.getData().get("email").toString());
                            user.setPassword(queryDocumentSnapshot.getData().get("password").toString());
                            break;
                        }
                    }
                }
            }
        });
        return user;
    }

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
