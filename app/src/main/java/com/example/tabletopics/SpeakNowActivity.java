package com.example.tabletopics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpeakNowActivity extends AppCompatActivity {

    private String theme = "";
    private Spinner spinner2;
    private Spinner spinner3;
    private long maximum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_now);
        Toolbar myToolbar=findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Speak Now");
        myToolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        spinner2 = findViewById(R.id.maxTimeSpinner);
        spinner3 = findViewById(R.id.themeSpinner);

        List<Long> maxTime = new ArrayList<>();
        for(long i = 1; i <= 15; i++){
            maxTime.add(i);
        }

        LayoutInflater inflater = getLayoutInflater();
        View topicsBankView = inflater.inflate(R.layout.activity_topics_bank, null);
        TableLayout layout = (TableLayout)topicsBankView.findViewById(R.id.topicsTable);

        List<String> themes = new ArrayList<>();
        for(int i = 0; i < layout.getChildCount(); i++){
            View v = layout.getChildAt(i);
            if(v instanceof TableRow){
                TableRow row = (TableRow)v;
                for(int j = 0; j < row.getChildCount(); j++){
                    if(row.getChildAt(j) instanceof Button) {
                        Button button = (Button)row.getChildAt(j);
                        String theme = button.getText().toString();
                        themes.add(theme);
                    }
                }
            }
        }

        String[] themesArr = new String[themes.size()];
        themesArr = themes.toArray(themesArr);

        Long[] timesArr = new Long[maxTime.size()];
        timesArr = maxTime.toArray(timesArr);

        ArrayAdapter<Long> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, timesArr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maximum = (long)parent.getItemAtPosition(position)*60000;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, themesArr);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                theme = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = (Button)findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(SpeakNowActivity.this, LoadingScreenActivity.class);
                intent.putExtra("theme", theme);
                intent.putExtra("maximum", maximum);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

}
