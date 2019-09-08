package com.example.tongan.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "Signup";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    TextInputLayout displaynameLayout;
    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;
    TextInputLayout confirmPasswordLayout;

    EditText displaynameText;
    EditText emailText;
    EditText passwordText;
    EditText confirmPasswordText;
    Button signUpButton;
    Button signUpCancel;

    String displayName;
    String email;
    String password;
    String confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpNewUserAndAddToDatabase();

            }
        });

        signUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });

    }

    public void signUpNewUserAndAddToDatabase() {
        if (!validateInputs()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "createUserWithEmail:success");
                            addUserInformationToDatabase(displayName, email, password, 0, 0.0);
                            openHomePage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                emailLayout.setError("Email address already in use.");
                            } else if (task.getException().getMessage().equals("The email address is badly formatted.")) {
                                emailLayout.setError("Invalid email address");
                            } else {
                                emailLayout.setErrorEnabled(false);
                            }

                            if (password.length() < 6) {
                                passwordLayout.setError("Password must be at least 6 characters");
                            } else {
                                passwordLayout.setErrorEnabled(false);
                            }

                        }

                    }
                });
    }

    public boolean validateInputs() {
        boolean valid = true;
        boolean isPasswordEmpty = false;
        displayName = displaynameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        confirmPassword = confirmPasswordText.getText().toString();

        if (TextUtils.isEmpty(displayName)) {
            displaynameLayout.setError("Display name cannot be empty");
            valid = false;
        } else {
            displaynameLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email cannot be empty");
            valid = false;
        } else {
            emailLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password cannot be empty");
            valid = false;
            isPasswordEmpty = true;
        } else {
            passwordLayout.setErrorEnabled(false);
        }

        if (!isPasswordEmpty) {
            if (!password.equals(confirmPassword)) {
                confirmPasswordLayout.setError("Password does not match");
                passwordLayout.setError("Password does not match");
                valid = false;
            } else {
                confirmPasswordLayout.setErrorEnabled(false);
                passwordLayout.setErrorEnabled(false);
            }
        }

        return valid;
    }

    public void init() {
        mAuth = FirebaseAuth.getInstance();

        displaynameLayout = findViewById(R.id.signup_displayname_layout);
        emailLayout = findViewById(R.id.signup_email_layout);
        passwordLayout = findViewById(R.id.signup_password_layout);
        confirmPasswordLayout = findViewById(R.id.signup_password_confirm_layout);
        signUpButton = findViewById(R.id.signup_submit);
        signUpCancel = findViewById(R.id.signup_cancel);

        displaynameText = findViewById(R.id.signup_displayname_text);
        emailText = findViewById(R.id.signup_email_text);
        passwordText = findViewById(R.id.signup_password_text);
        confirmPasswordText = findViewById(R.id.signup_password_confirm_text);

    }

    public void openLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addUserInformationToDatabase(String displayName, String email, String password, int followers, double ratings) {
        User user = new User(displayName, email, password, followers, ratings);

//        Task<DocumentReference> a = db.collection("Users").add(user);
        // use unique id as document in firestore
//        db.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Log.d(TAG, "DocumentSnapshot successfully written!");
//                String id = documentReference.getId();
//                DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(id);
//                doc.update("id", id);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });

        db.collection("Users").document(email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                        openLoginPage();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}
