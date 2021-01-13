package com.example.tabletopics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadingScreenActivity extends AppCompatActivity {

    TextView textView;
    List<String> prompts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_loading_screen);

        //textView = findViewById(R.id.prompt);

        textView.setText("Ready?");

        textView.setText("Set...");

    }
}