package com.example.celebrityquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText edtEmail, edtPassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide(); //액션바 제거

        edtEmail = (EditText)findViewById(R.id.edt_email);
        edtPassword = (EditText)findViewById(R.id.edt_password);

        loginButton = (Button)findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);

        register = (TextView)findViewById(R.id.txt_register);
        register.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance(); //firebase에 인증된 값들을 가져와줌

       progressBar = (ProgressBar) findViewById(R.id.progressBar);

       progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_register:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break;
            case R.id.btn_login:
                 userLogin();
                 break;
        }
    }

    private void userLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if(email.isEmpty()){
            edtEmail.setError("이메일을 입력해주세요");
            edtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("올바른 이메일 형식을 입력해주세요.");
            edtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edtPassword.setError("비밀번호를 입력해주세요");
            edtPassword.requestFocus();
            return;
        }

        if(password.length() < 8){
            edtPassword.setError("비밀번호를 문자 + 숫자형식으로 8자 이상 입력하세요.");
            edtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

         mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                     if(user.isEmailVerified()){
                     Intent intent  = new Intent(LoginActivity.this, MainActivity.class);
                     startActivity(intent);
                     } else {
                         user.sendEmailVerification();
                         Toast.makeText(LoginActivity.this, "이메일로 접속하여 인증을 수락해주세요.", Toast.LENGTH_LONG).show();
                     }
                 }else{
                     Toast.makeText(LoginActivity.this, "유효하지 않은 정보입니다!", Toast.LENGTH_LONG).show(); //로그인 실패
                     progressBar.setVisibility(View.INVISIBLE);
                 }
             }
         });
    }
}
