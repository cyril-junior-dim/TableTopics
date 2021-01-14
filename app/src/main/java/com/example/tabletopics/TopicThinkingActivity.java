package com.example.tabletopics;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class TopicThinkingActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 30000;

    TextView title;
    TextView timerText;
    TextView ret;
    TextToSpeech textToSpeech;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String theme = "";
    Random random = new Random();
    ImageButton audio;

    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_thinking);

        title = (TextView) findViewById(R.id.speechTitle);
        timerText = (TextView) findViewById(R.id.timertext);
        ret = (TextView) findViewById(R.id.ret);
        audio = (ImageButton) findViewById(R.id.audio);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            theme = bundle.getString("theme");
        }

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("textToSpeech", "Language not supported");
                    } else {
                        audio.setEnabled(true);
                    }
                } else {
                    Log.e("textToSpeech", "Initialization failed");
                }
            }
        });

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
                            String randomElement = "'" + topics.get(randomIndex)  + "'";
                            title.setText(randomElement);
                            startTimer();
                        } else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                Intent intent = new Intent(v.getContext(), SpeakNowActivity.class);
                startActivity(intent);
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }

    private void speak(){
        String toSpeak = title.getText().toString();
        textToSpeech.speak("test", TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timerText.setVisibility(View.GONE);
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerText.setText(timeLeftFormatted);
    }
}