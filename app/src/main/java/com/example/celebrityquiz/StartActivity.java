package com.example.celebrityquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.Random;

public class StartActivity extends AppCompatActivity {
    Button btn_start;
    Button btn_setting;
    Button btn_chkscore; //점수 확인 버튼

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Objects.requireNonNull(getSupportActionBar()).hide(); //액션바 숨김

        btn_start= (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                Random random = new Random();

                intent.putExtra("level", random.nextInt(3));
                intent.putExtra("seconds",random.nextInt(90));
                startActivity(intent);
            }
        });

        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_chkscore = (Button)findViewById(R.id.btn_chkscore);
        btn_chkscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
