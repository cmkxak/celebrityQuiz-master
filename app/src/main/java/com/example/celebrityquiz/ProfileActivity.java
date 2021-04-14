package com.example.celebrityquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference ref;
    private String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Objects.requireNonNull(getSupportActionBar()).hide(); //액션바 제거

        LinearLayout userinfolayout = findViewById(R.id.container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sub_profile, userinfolayout,true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView nickNameTextView = (TextView) findViewById(R.id.txt_nickname);
        final TextView emailTextView = (TextView) findViewById(R.id.txt_emailaddress);
        TextView scoreTextView = (TextView) findViewById(R.id.txt_scores);

        ref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String nickName = userProfile.nickName;
                    String email = userProfile.email;

                    emailTextView.setText("이메일 주소    " + email);
                    nickNameTextView.setText("닉네임             " + nickName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "잘못된 정보입니다.", Toast.LENGTH_LONG).show();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("scores", 0);
        int score = sharedPreferences.getInt("scores", 0);
        scoreTextView.setText(String.valueOf(score) + "점");


    }
}
