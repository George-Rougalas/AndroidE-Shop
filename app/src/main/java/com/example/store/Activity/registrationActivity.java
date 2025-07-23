package com.example.store.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.store.R;
import com.example.store.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class registrationActivity extends AppCompatActivity {

    TextInputEditText email, password, name;
    Button signup;

    FirebaseAuth auth;

    FirebaseDatabase database;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firestore
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        signup = findViewById(R.id.buttonReg);

        // Initialize UI elements
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        name = findViewById(R.id.editTextUsername);


        // Set click listener for login button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                createUser();
            }
        });
    }

    public void login(View view){
        startActivity(new Intent(registrationActivity.this, loginActivity.class));
    }

    public void registration(View view){
        startActivity(new Intent(registrationActivity.this, registrationActivity.class));
    }

    private void createUser() {
        String name = Objects.requireNonNull(this.name.getText()).toString();
        String password = Objects.requireNonNull(this.password.getText()).toString();
        String email = Objects.requireNonNull(this.email.getText()).toString();
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();


        if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Field is Empty!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.length() < 6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
        //                    UserModel userModel = new UserModel(name,  email, password);
        //                    String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
        //                    database.getReference().child("Users").child(id).setValue(userModel);

                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                UserModel userModel = new UserModel(name, email, password);
                                String userId = user.getUid();
                                database.getReference().child("Users").child(userId).setValue(userModel);

                                //extra code to add name to shared preferences
                                SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("FullName", name);
                                editor.apply();
                            }

                            Toast.makeText(registrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            // Optionally, navigate to login screen or main activity
                            startActivity(new Intent(registrationActivity.this, loginActivity.class));
                            finish(); // Finish current activity to avoid returning here
                        }
                        else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(registrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        }
        });



    }

    private void updateUI(FirebaseUser user) {

    }
}
