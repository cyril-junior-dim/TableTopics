package com.example.tabletopics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TopicsListActivity extends AppCompatActivity {
    TextView title;
    ListView listView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    List<String> topics = new ArrayList<>();
    String buttonTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_list);

        title = (TextView) findViewById(R.id.std_textview);
        listView = (ListView) findViewById(R.id.my_list_view);

        //text from calling button
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            buttonTitle = bundle.getString("name");
        }

        //toolbar
        Toolbar myToolbar=findViewById(R.id.my_toolbar);
        myToolbar.setTitle(buttonTitle);
        myToolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TopicsBankActivity.class));
                finish();
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        fStore.setFirestoreSettings(settings);

        fStore.collection("topics")
                .whereEqualTo("category", buttonTitle)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                                topics.add(document.getString("title"));
                            }
                        } else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                        String[] myArray = new String[topics.size()];
                        topics.toArray(myArray);
                        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,android.R.id.text1, myArray);
                        listView.setAdapter(adapter);
                    }
                });
    }
}