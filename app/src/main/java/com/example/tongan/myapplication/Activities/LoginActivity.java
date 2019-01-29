package com.example.tongan.myapplication.Activities;

import android.content.Intent;
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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private FirebaseAuth mAuth;

    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    EditText emailText;
    EditText passwordText;

    String email;
    String password;

    Button signUpButton;
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpPage();
            }
        });
    }

    public void init() {
        mAuth = FirebaseAuth.getInstance();

        emailLayout = findViewById(R.id.login_email_layout);
        passwordLayout = findViewById(R.id.login_password_layout);

        emailText = findViewById(R.id.login_email_text);
        passwordText = findViewById(R.id.login_password_text);

        signUpButton = findViewById(R.id.signup_button);
        signInButton = findViewById(R.id.login_button_login);
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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            openHomePage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
