package com.example.tabletopics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditInfoActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private Button cEmail, cName, cPwd, submit;
    private EditText email, name, pwd, confPwd;
    private  String mail, fname, userID, password;
    private  ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        Toolbar myToolbar=findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Account");
        myToolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }
        });

        cEmail = findViewById(R.id.cEmail);
        cName = findViewById(R.id.cName);
        cPwd = findViewById(R.id.cPwd);
        submit = findViewById(R.id.sub);
        email = findViewById(R.id.emailAddress);
        name = findViewById(R.id.fullName);
        pwd = findViewById(R.id.password);
        confPwd = findViewById(R.id.confirmPassword);
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();


        cEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        mail = email.getText().toString().trim();

                        if(TextUtils.isEmpty(mail)){
                            email.setError("Field required!");
                            return;
                        }

                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, String> map = new HashMap<>();
                        map.put("email", mail);
                        documentReference.set(map, SetOptions.merge());

                        user.updateEmail(mail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditInfoActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            email.setVisibility(View.GONE);
                                            submit.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                });
            }
        });

        cName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        fname = name.getText().toString().trim();

                        if(TextUtils.isEmpty(fname)){
                            name.setError("Field required!");
                            return;
                        }

                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, String> map = new HashMap<>();
                        map.put("full_Name", fname);
                        documentReference.set(map, SetOptions.merge());

                        Toast.makeText(EditInfoActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        name.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                    }
                });
            }
        });

        cPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd.setVisibility(View.VISIBLE);
                confPwd.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        password = pwd.getText().toString().trim();

                        if(TextUtils.isEmpty(password)){
                            pwd.setError("Password is required");
                            return;
                        }

                        if(password.length() < 6){
                            pwd.setError("Password must be at least 6 characters long");
                            return;
                        }

                        if(!pwd.getText().toString().trim().equals(confPwd.getText().toString().trim())){
                            confPwd.setError("Passwords do not match");
                            return;
                        }

                        user.updatePassword(password)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditInfoActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            pwd.setVisibility(View.GONE);
                                            confPwd.setVisibility(View.GONE);
                                            submit.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                });
            }
        });

    }
}