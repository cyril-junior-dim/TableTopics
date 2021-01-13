package com.example.tabletopics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class TopicsBankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_bank);
        Toolbar myToolbar=findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Topics Bank");
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

        Button contests = findViewById(R.id.contests);
        contests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsListActivity.class);
                String text = contests.getText().toString();
                intent.putExtra("name", text);
                startActivity(intent);
            }
        });

        Button technology = findViewById(R.id.technology);
        technology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsListActivity.class);
                String text = technology.getText().toString();
                intent.putExtra("name", text);
                startActivity(intent);
            }
        });

        Button sport = findViewById(R.id.sport);
        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsListActivity.class);
                String text = sport.getText().toString();
                intent.putExtra("name", text);
                startActivity(intent);
            }
        });

        Button comedy = findViewById(R.id.comedy);
        comedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsListActivity.class);
                String text = comedy.getText().toString();
                intent.putExtra("name", text);
                startActivity(intent);
            }
        });

        Button history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsListActivity.class);
                String text = history.getText().toString();
                intent.putExtra("name", text);
                startActivity(intent);
            }
        });

        Button travel = findViewById(R.id.travel);
        travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsListActivity.class);
                String text = travel.getText().toString();
                intent.putExtra("name", text);
                startActivity(intent);
            }
        });

        Button current_affairs = findViewById(R.id.current_affairs);
        current_affairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsListActivity.class);
                String text = current_affairs.getText().toString();
                intent.putExtra("name", text);
                startActivity(intent);
            }
        });

        Button love = findViewById(R.id.love);
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsListActivity.class);
                String text = love.getText().toString();
                intent.putExtra("name", text);
                startActivity(intent);
            }
        });

        Button fantasy = findViewById(R.id.fantasy);
        fantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsListActivity.class);
                String text = fantasy.getText().toString();
                intent.putExtra("name", text);
                startActivity(intent);
            }
        });
    }
}
