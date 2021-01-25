package com.example.tabletopics;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeaderboardActivity extends AppCompatActivity {
    TextView textView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    List<String> users = new ArrayList<>();
    StringBuilder data = new StringBuilder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Toolbar myToolbar=findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Leaderboard");
        myToolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        textView = findViewById(R.id.LBStats);

        fStore.collection("users")
                .orderBy("speech_count", Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                StringBuilder abb = new StringBuilder();
                                String[] name = Objects.requireNonNull(document.getString("full_Name")).split("\\s+");

                                for(int i = 0; i < name.length - 1; i++){
                                    abb.append(name[i].charAt(0)).append(". ");
                                }
                                abb.append(name[name.length - 1]);

                                MyPair user = new MyPair(abb.toString(), document.getLong("speech_count").toString());
                                users.add(user.toString());
                            }
                        } else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                        for(int i = 0; i < users.size(); i++){
                            data.append(users.get(i));
                        }
                        textView.setText(data.toString());
                        textView.setMovementMethod(new ScrollingMovementMethod());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }
}