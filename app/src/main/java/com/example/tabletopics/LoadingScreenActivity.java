package com.example.tabletopics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadingScreenActivity extends AppCompatActivity {
    private TextSwitcher textSwitcher;
    private Button nextButton;
    private int stringIndex = 0;
    int count = 0;
    private String[] prompts = {"Ready?", "Set...", "GO!"};
    private TextView textView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        textSwitcher = findViewById(R.id.textSwitcher);

        nextButton = findViewById(R.id.magicButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringIndex == prompts.length - 1) {
                    Intent intent = new Intent(v.getContext(), TopicThinkingActivity.class);
                    startActivity(intent);
                } else {
                    textSwitcher.setText(prompts[++stringIndex]);
                }
            }
        });

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView = new TextView(LoadingScreenActivity.this);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(64);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                return textView;
            }
        });

        textSwitcher.setText(prompts[stringIndex]);
        handler.postDelayed(buttonClicker, 1150);
    }

    private Runnable buttonClicker = new Runnable() {
        @Override
        public void run() {
            nextButton.performClick();
            if (count++ < prompts.length - 1) {
                handler.postDelayed(this, 1150);
            }
        }
    };
}