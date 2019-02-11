package com.example.tongan.myapplication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tongan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import javax.net.ssl.SSLSessionContext;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth fireBaseAuth;

    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    EditText emailText;
    EditText passwordText;

    String email;
    String password;

    Button signUpButton;
    Button signInButton;

    TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        // auto sign in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            openHomePage();
        }

        // sign in button clicked
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        // sign up button clicked
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpPage();
            }
        });

        // forget password button clicked
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = findViewById(R.id.login_email_text);
                email = emailText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    emailLayout.setError("Please enter your email");
                } else {
                    emailLayout.setErrorEnabled(false);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Email reset");
                        }
                    });
                }
            }
        });
    }


    public void init() {
        fireBaseAuth = FirebaseAuth.getInstance();

        emailLayout = findViewById(R.id.login_email_layout);
        passwordLayout = findViewById(R.id.login_password_layout);

        emailText = findViewById(R.id.login_email_text);
        passwordText = findViewById(R.id.login_password_text);

        signUpButton = findViewById(R.id.signup_button);
        signInButton = findViewById(R.id.login_button_login);

        forgetPassword = findViewById(R.id.forget_password);
    }

    public boolean validateInputs() {
        boolean valid = true;

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

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

    public void openSignUpPage() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void openHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void signInUser() {
        if (!validateInputs()) {
            return;
        }

        fireBaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    DocumentReference doc = FirebaseFirestore.getInstance().collection("Users").document(email);
                    doc.update("password", password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            openHomePage();
                        }
                    });
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    passwordLayout.setError("Password does not match");
                    //Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
