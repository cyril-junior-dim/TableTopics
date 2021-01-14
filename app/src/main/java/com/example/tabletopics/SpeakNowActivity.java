package com.example.tabletopics;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import 	androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpeakNowActivity extends AppCompatActivity {

    private String theme = "";
    private Spinner spinner;
    private Spinner spinner2;
    private Spinner spinner3;

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

        spinner = findViewById(R.id.minTimeSpinner);
        spinner2 = findViewById(R.id.maxTimeSpinner);
        spinner3 = findViewById(R.id.themeSpinner);

        List<Integer> minTime = new ArrayList<>();
        for(int i = 0; i <= 10; i++){
            minTime.add(i);
        }

        List<Integer> maxTime = new ArrayList<>();
        for(int i = 1; i <= 15; i++){
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

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, minTime);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, maxTime);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, themesArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
                Intent intent = new Intent(v.getContext(), LoadingScreenActivity.class);
                intent.putExtra("theme", theme);
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
