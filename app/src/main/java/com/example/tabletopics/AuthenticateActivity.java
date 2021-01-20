package com.example.tabletopics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AuthenticateActivity extends AppCompatActivity {

    EditText pwd, email;
    Button button;
    FirebaseAuth fAuth;
    String password, mail;
    TextView textView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        Toolbar myToolbar=findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Edit Profile");
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

        pwd = findViewById(R.id.password);
        email = findViewById(R.id.email);
        button = findViewById(R.id.sub);
        fAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = email.getText().toString();
                password = pwd.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                AuthCredential credential = EmailAuthProvider
                        .getCredential(mail, password);

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("TAG", "User re-authenticated.");
                                if(task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(), EditInfoActivity.class));
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(AuthenticateActivity.this, "Error! " + task.getException().getMessage() + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }
        });
    }
}