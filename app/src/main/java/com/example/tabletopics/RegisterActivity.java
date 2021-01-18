package com.example.tabletopics;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    EditText mFirstName, mLastName, mEmail, mPassword, mConfirmPassword;
    Button mRegistrationBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID, email, password, firstName, lastName;
    Boolean nameTaken = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //region Instantiation
        mFirstName = findViewById(R.id.firstName);
        mLastName = findViewById(R.id.lastName);
        mEmail = findViewById(R.id.emailAddress);
        mPassword = findViewById(R.id.password);
        mRegistrationBtn = findViewById(R.id.register);
        mLoginBtn = findViewById(R.id.createText);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBarHome);
        //endregion
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        mRegistrationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                firstName = mFirstName.getText().toString();
                lastName = mLastName.getText().toString();

                if(TextUtils.isEmpty(firstName)){
                    mFirstName.setError("Name required for Leaderboard");
                    return;
                }

                if(TextUtils.isEmpty(lastName)){
                    mLastName.setError("Surname required for Leaderboard");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must be at least 6 characters long");
                    return;
                }

                if(!mPassword.getText().toString().trim().equals(mConfirmPassword.getText().toString().trim())){
                    mConfirmPassword.setError("Passwords do not match");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Register user with Firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("full_Name", firstName);
                            user.put("username", lastName);
                            user.put("email",email);
                            user.put("speech_count", 0);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: user Profile created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: "+ e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }

}