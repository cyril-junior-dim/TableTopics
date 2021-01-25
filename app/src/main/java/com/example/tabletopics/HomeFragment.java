package com.example.tabletopics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {
    TextView textView;
    FirebaseAuth fAuth;
    Button button, button2, button3, button4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textView = view.findViewById(R.id.notVerified);
        fAuth = FirebaseAuth.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();

        assert user != null;
        if(user.isEmailVerified()){
            textView.setVisibility(View.VISIBLE);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Send verification link
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "A verification email has been sent to your mailbox", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "onFailure: Email not sent "+ e.getMessage());
                        }
                    });
                }
            });
        }

        button = view.findViewById(R.id.goToSpeakNow);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SpeakNowActivity.class);
                startActivity(intent);
            }
        });

        button2 = view.findViewById(R.id.goToLeaderboard);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        button3 = view.findViewById(R.id.goToTopicsBank);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TopicsBankActivity.class);
                startActivity(intent);
            }
        });

        button4 = view.findViewById(R.id.goToCoaching);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CoachingActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
