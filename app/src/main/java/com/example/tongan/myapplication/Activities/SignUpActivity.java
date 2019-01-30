package com.example.tongan.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tongan.myapplication.Classes.User;
import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "Signup";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    TextInputLayout firstNameLayout;
    TextInputLayout lastNameLayout;
    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    EditText firstNameText;
    EditText lastNameText;
    EditText emailText;
    EditText passwordText;
    Button signUpButton;

    String firstName;
    String lastName;
    String email;
    String password;


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
                            addUserInformationToDatabase(firstName, lastName, email, password, 0, 0.0);
                            openHomePage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            if (task.getException().getMessage().equals("The email address is badly formatted.")) {
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
        firstName = firstNameText.getText().toString();
        lastName = lastNameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            firstNameLayout.setError("First name cannot be empty");
            valid = false;
        } else {
            firstNameLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(lastName)) {
            lastNameLayout.setError("Last name cannot be empty");
            valid = false;
        } else {
            lastNameLayout.setErrorEnabled(false);
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
        } else {
            passwordLayout.setErrorEnabled(false);
        }

        return valid;
    }

    public void init() {
        mAuth = FirebaseAuth.getInstance();

        firstNameLayout = findViewById(R.id.signup_firstName_layout);
        lastNameLayout = findViewById(R.id.signup_lastName_layout);
        emailLayout = findViewById(R.id.signup_email_layout);
        passwordLayout = findViewById(R.id.signup_password_layout);
        signUpButton = findViewById(R.id.signup_submit);

        firstNameText = findViewById(R.id.signup_firstName_text);
        lastNameText = findViewById(R.id.signup_lastName_text);
        emailText = findViewById(R.id.signup_email_text);
        passwordText = findViewById(R.id.signup_password_text);

    }

    public void openLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addUserInformationToDatabase(String firstName, String lastName, String email, String password, int followers, double ratings) {
        User user = new User(firstName, lastName, email, password, followers, ratings);
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
