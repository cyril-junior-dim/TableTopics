package com.example.tabletopics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AfterSpeechActivity extends AppCompatActivity {
    private String time = "";
    private TextView textView, skipText, retryText;
    private String status, userID, X;
    private long count;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_speech);

        retryText = findViewById(R.id.retry);
        textView = findViewById(R.id.stats);
        skipText = findViewById(R.id.skipText);
        Intent intent = getIntent();
        time = intent.getStringExtra("time") + "\n";
        status = intent.getStringExtra("status");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(status.equals("Overtime\n")){
                    count = documentSnapshot.getLong("speech_count");
                    generateStats(count);
                }
                else if(status.equals("Under Time")){
                    count = documentSnapshot.getLong("speech_count");
                    generateStats(count);
                }
                else{
                    count = documentSnapshot.getLong("speech_count") + 1;
                    updateStats(count);
                }
            }
        });

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<Object, Long> map = new HashMap<>();
                    map.put("speech_count", count);
                    documentReference.set(map, SetOptions.merge());
                }
            }
        });

        retryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SpeakNowActivity.class);
                startActivity(intent);
            }
        });

        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void generateStats(long x) {
        X = "Speeches Completed:\t" + x + "\n";
        String data = time + X + status;
        textView.setText(data);
    }

    public void updateStats(long x){
        X = "Speeches Completed:\t" + (x - 1) + "\n";
        String data = time + X + status;
        textView.setText(data);
    }
}