package com.example.tabletopics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingScreenActivity extends AppCompatActivity {
    private TextSwitcher textSwitcher;
    private Button nextButton;
    private int stringIndex = 0;
    int count = 0;
    String theme = "";
    Long maximum;
    private final String[] prompts = {"Ready?", "Set...", "GO!"};
    private TextView textView;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        Intent intent = getIntent();
        theme = intent.getStringExtra("theme");
        maximum = intent.getLongExtra("maximum", 1);

        textSwitcher = findViewById(R.id.textSwitcher);

        nextButton = findViewById(R.id.magicButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringIndex == prompts.length - 1) {
                    Intent intent = new Intent(v.getContext(), SpeakingActivity.class);
                    intent.putExtra("theme", theme);
                    intent.putExtra("maximum", maximum);
                    startActivity(intent);
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

    private final Runnable buttonClicker = new Runnable() {
        @Override
        public void run() {
            nextButton.performClick();
            if (count++ < prompts.length - 1) {
                handler.postDelayed(this, 1150);
            }
        }
    };

    @Override
    public void onBackPressed() {

    }
}