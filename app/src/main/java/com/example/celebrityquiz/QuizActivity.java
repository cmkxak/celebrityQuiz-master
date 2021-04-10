package com.example.celebrityquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    // Declare variables
    private List<Quiz> quizList;
    private int seconds;
    private int indexCurrentQuestion;

    private TextView questionView;
    private ImageView imageView;
    private RadioGroup radioGroup;
    private RadioButton radioButtonOne;
    private RadioButton radioButtonTwo;
    private RadioButton radioButtonThree;
    private RadioButton radioButtonFour;
    private Button buttonPrevious;
    private Button buttonNext;
    private TextView textTime;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_quiz);

        // Hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Define Activity views
        questionView = findViewById(R.id.celebrityQuestion);
        imageView = findViewById(R.id.celebrityImage);
        radioGroup = findViewById(R.id.celebrityOption);
        radioButtonOne = findViewById(R.id.radioButtonOne);
        radioButtonTwo = findViewById(R.id.radioButtonTwo);
        radioButtonThree = findViewById(R.id.radioButtonThree);
        radioButtonFour = findViewById(R.id.radioButtonFour);
        textTime = findViewById(R.id.textTime);

        // setOnClickListener and set checked onClick for each button
        radioButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioButton) view).setChecked(true);
                quizList.get(indexCurrentQuestion).userAnswer = 1;
            }
        });

        radioButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioButton) view).setChecked(true);
                quizList.get(indexCurrentQuestion).userAnswer = 2;
            }
        });

        radioButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioButton) view).setChecked(true);
                quizList.get(indexCurrentQuestion).userAnswer = 3;
            }
        });

        radioButtonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioButton) view).setChecked(true);
                quizList.get(indexCurrentQuestion).userAnswer = 4;
            }
        });

        // Define button views
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrevious = findViewById(R.id.buttonPrevious);

        // Access intent interface and get variables
        Intent intent = getIntent();
        int level = intent.getIntExtra("level", new Random().nextInt(3));
        seconds = intent.getIntExtra("seconds", new Random().nextInt(90));
        String string = null;


        // Safely read data from saved file
        try {
            FileInputStream fileInputStream = openFileInput("myJson"); //파일 불러오기
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8); //바이트 단위로 데이터를 읽어들여서 문자단위로 읽음
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //데이터 처리 속도 향상을 위한 BufferedReader, 데이터를 묶어서 한번에 처리
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            string = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Quiz>>(){}.getType();
        List<Quiz> list = gson.fromJson(string, type);

        // Set sublist based on user set level
        if (level == 1) {
            assert list != null;
            quizList = list.subList(0, 5);
        } else if (level == 2) {
            assert list != null;
            quizList = list.subList(5, 10);
        } else {
            assert list != null;
            quizList = list.subList(11, 16);
        }

        // initialise and set for each index in current activity as current question (현재 문제에 관한 현재 액티비티 인덱스 설정 및 인덱스 초기화)
        indexCurrentQuestion = 0;
        Quiz currentQuestion = quizList.get(indexCurrentQuestion);
        currentQuestionView(currentQuestion);
        buttonPrevious.setEnabled(false); // Disable previous button when current index is 0

        // See function
        startTimer();

        // When user submit quiz, stop time and start Solution Activity
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                Intent i = new Intent(QuizActivity.this, SolutionActivity.class);
                i.putExtra("score", getScore());
                // Change List to ArrayList to accommodate subList
                ArrayList<Quiz> list = new ArrayList<>(quizList);
                i.putExtra("quizList", list);
                i.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );
                startActivity(i);
            }
        });

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SharedPreferences sharedPreferences = getSharedPreferences("scores", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        int scores = getScore();
//        editor.putInt("scores", scores);
//        editor.commit();
//    }

    // Start countdown. OnFinish, start Solution Activity
    public void startTimer() {
        textTime.setText(String.valueOf(seconds));
        //1초 단위로 타이머가 작동하도록 되어있다.
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) { //millisinFuture = 제한시간, countDownInterval은 몇초마다 타이머가 작동할 것인지
            @Override
            public void onTick(long millisUntilFinished) {
                textTime.setText(String.valueOf((int) (millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(QuizActivity.this, SolutionActivity.class);
                i.putExtra("score", getScore());
                // Change List to ArrayList to accommodate subList
                ArrayList<Quiz> list = new ArrayList<>(quizList);
                i.putExtra("quizList", list);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        }.start();
    }

    // Cancel timer to prevent countDown in background
    // If not defined, Solution Activity will start even when user goes back to
    // Main Activity because Quiz Activity doesn't get destroyed instantly
    public void stopTimer() {
        countDownTimer.cancel();
    }

    // Pre-define new views before setting previous question as current question, for index < 0
    public void onButtonPrevious(View view) {
        if(indexCurrentQuestion != 0) {
            indexCurrentQuestion--;
            if(indexCurrentQuestion == 0) buttonPrevious.setEnabled(false);
            if(indexCurrentQuestion != (quizList.size() - 1)) buttonNext.setEnabled(true);
            Quiz currentQuestion = quizList.get(indexCurrentQuestion);
            currentQuestionView(currentQuestion);

            radioGroup = findViewById(R.id.celebrityOption);
            if(currentQuestion.userAnswer == 0) radioGroup.clearCheck();
            else {
                switch (currentQuestion.userAnswer) {
                    case 1: {
                        radioGroup.check(R.id.radioButtonOne);
                        break;
                    }
                    case 2: {
                        radioGroup.check(R.id.radioButtonTwo);
                        break;
                    }
                    case 3: {
                        radioGroup.check(R.id.radioButtonThree);
                        break;
                    }
                    case 4: {
                        radioGroup.check(R.id.radioButtonFour);
                        break;
                    }
                }
            }
        }
    }

    // Pre-define new views before setting next question as current question, for index > list.size()
    public void onButtonNext(View view) {
        if(indexCurrentQuestion != (quizList.size() - 1)) {
            indexCurrentQuestion++;
            if(indexCurrentQuestion == (quizList.size() - 1)) buttonNext.setEnabled(false);
            if(indexCurrentQuestion != 0) buttonPrevious.setEnabled(true);
            Quiz currentQuestion = quizList.get(indexCurrentQuestion);
            currentQuestionView(currentQuestion);

            radioGroup = findViewById(R.id.celebrityOption);
            if(currentQuestion.userAnswer == 0) radioGroup.clearCheck();
            else {
                switch (currentQuestion.userAnswer) {
                    case 1: {
                        radioGroup.check(R.id.radioButtonOne);
                        break;
                    }
                    case 2: {
                        radioGroup.check(R.id.radioButtonTwo);
                        break;
                    }
                    case 3: {
                        radioGroup.check(R.id.radioButtonThree);
                        break;
                    }
                    case 4: {
                        radioGroup.check(R.id.radioButtonFour);
                        break;
                    }
                }
            }
        }
    }
    public void currentQuestionView(Quiz currentQuestion) {
        questionView.setText(String.format("%s. %s", indexCurrentQuestion + 1, currentQuestion.question));
        radioButtonOne.setText(currentQuestion.one);
        radioButtonTwo.setText(currentQuestion.two);
        radioButtonThree.setText(currentQuestion.three);
        radioButtonFour.setText(currentQuestion.four);
        Glide.with(imageView.getContext()).load(currentQuestion.imageUrl).into(imageView);
    }

    // Calculate score
    public int getScore() {
        int score = 0;
        for (int i = 0; i < quizList.size(); i++) {
            if (quizList.get(i).userAnswer == quizList.get(i).correctAnswer) score++;
        }
        return score;
    }


}
