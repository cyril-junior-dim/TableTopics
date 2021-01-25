package com.example.tabletopics;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

public class SpeakingActivity extends AppCompatActivity{

    //region Class Variables
    private long mTimeLeftInMillis = 30000;
    private long maxTime;
    private long pauseOffset;
    boolean running;
    private FirebaseFirestore fStore;
    private String theme = "";
    private Random random = new Random();
    private String completionStatus = "Under Time";

    //endregion

    //region Views
    private ConstraintLayout constraintLayout;
    private CountDownTimer mCountDownTimer;
    private Chronometer chronometer;
    private TextView title, timerText, ret, tip;
    private ImageView imageView;
    private EditText mEditText;
    private TextToSpeech tts;
    private ValueAnimator valueAnimator;
    private ImageButton audio;
    private Button skip, btnCircle;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking);

//region Instantiation
        title = findViewById(R.id.speechTitle);
        timerText = findViewById(R.id.timertext);
        ret = findViewById(R.id.ret);
        tip = findViewById(R.id.textView18);
        audio = findViewById(R.id.audio);
        skip = findViewById(R.id.skip);
        chronometer = findViewById(R.id.Timer);
        fStore = FirebaseFirestore.getInstance();
        mEditText = findViewById(R.id.edit_text);
        imageView = findViewById(R.id.clock);
        btnCircle = findViewById(R.id.btnCircle);
        constraintLayout = findViewById(R.id.myLayout);

        Intent intent = getIntent();
        theme = intent.getStringExtra("theme");
        maxTime = intent.getLongExtra("maximum", 1);

//endregion

        skip.setEnabled(false);

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
                            skip.setEnabled(true);
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
                stopCountdown();
                startChronometer(chronometer);
            }
        });

        btnCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseChronometer(chronometer);
                String timeUsed = "Speech Time:\t" + chronometer.getText();
                Intent intent = new Intent(v.getContext(), AfterSpeechActivity.class);
                intent.putExtra("time", timeUsed);
                intent.putExtra("status", completionStatus);
                startActivity(intent);
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

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if((SystemClock.elapsedRealtime() - chronometer.getBase()) < maxTime - 60000 ){
                    completionStatus = "Under Time";
                }

                if((SystemClock.elapsedRealtime() - chronometer.getBase()) >= maxTime - 60000 ){
                    completionStatus = "Minimum Time Reached";
                    constraintLayout.setBackgroundResource(R.drawable.background_green);
                    btnCircle.setBackgroundResource(R.drawable.btn_circle_background_green);
                }

                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= maxTime - 30000 ){
                    constraintLayout.setBackgroundResource(R.drawable.background_amber);
                    completionStatus = "Optimum Timing";
                    btnCircle.setBackgroundResource(R.drawable.btn_circle_background_amber);
                }

                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= maxTime ){
                    constraintLayout.setBackgroundResource(R.drawable.background_red);
                    completionStatus = "Maximum Time Reached";
                    btnCircle.setBackgroundResource(R.drawable.btn_circle_background_black);
                }

                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= maxTime + 30000 ){
                    completionStatus = "Overtime";
                }
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

    public void startChronometer(View v){
        startAnimation();
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v){
        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
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
                updateCountDownText(timerText);
            }
            @Override
            public void onFinish() {
                stopCountdown();
                startChronometer(chronometer);
            }
        }.start();
    }

    private void stopCountdown(){
        valueAnimator.start();
        timerText.setVisibility(View.GONE);
        audio.setVisibility(View.GONE);
        tip.setVisibility(View.GONE);
        skip.setVisibility(View.GONE);
        mCountDownTimer.cancel();
        imageView.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.VISIBLE);

    }

    private void updateCountDownText(TextView textView) {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textView.setText(timeLeftFormatted);
    }

    private void startAnimation(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.scale_up2);

        chronometer.startAnimation(animation);
        title.startAnimation(animation2);
        btnCircle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

}