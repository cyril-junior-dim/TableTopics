package com.example.tabletopics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TopicThinkingActivity extends AppCompatActivity {
    TextView title;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String theme = "";
    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_thinking);

        title = (TextView) findViewById(R.id.speechTitle);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            theme = bundle.getString("theme");
        }

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fStore.collection("topics")
                .whereEqualTo("category", theme)
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<String> topics = new ArrayList<>();
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                topics.add(document.getString("title"));
                            }
                            int randomIndex = random.nextInt(topics.size());
                            String randomElement = topics.get(randomIndex);
                            title.setText(randomElement);
                        } else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }

                    }
                });
    }
}