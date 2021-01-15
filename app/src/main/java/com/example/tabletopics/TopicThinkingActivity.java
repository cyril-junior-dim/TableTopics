package com.example.tabletopics;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class TopicThinkingActivity extends AppCompatActivity{

    //region Class Variables
    private static final long START_TIME_IN_MILLIS = 30000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer;
    private TextView title, timerText, ret, tip;
    private EditText mEditText;
    private TextToSpeech tts;
    private ValueAnimator valueAnimator;
    private FirebaseFirestore fStore;
    private String theme = "";
    private Random random = new Random();
    private ImageButton audio;
    private Button skip;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_thinking);

//region Instantiation
        title = findViewById(R.id.speechTitle);
        timerText = findViewById(R.id.timertext);
        ret = findViewById(R.id.ret);
        tip = findViewById(R.id.textView18);
        audio = findViewById(R.id.audio);
        skip = findViewById(R.id.skip);
        //fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mEditText = findViewById(R.id.edit_text);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            theme = bundle.getString("theme");
        }
//endregion

//region Query Database
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
//endregion

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.UK);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        audio.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.onFinish();
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

        valueAnimator = ValueAnimator.ofFloat(1f, 0f);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                timerText.setAlpha(alpha);
                tip.setAlpha(alpha);
                audio.setAlpha(alpha);
                skip.setAlpha(alpha);
            }
        });

    }

    private void speak() {
        mEditText.setText(title.getText());
        String text = mEditText.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
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
                valueAnimator.start();
                //timerText.setVisibility(View.GONE);
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