package com.example.celebrityquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView banner;
    private Button registerUser;
    private EditText edtNickname, edtEmail, edtPassword;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeruser);

        Objects.requireNonNull(getSupportActionBar()).hide(); //액션바 제거
        mAuth = FirebaseAuth.getInstance();

        banner = (TextView)findViewById(R.id.txt_banner);
        banner.setOnClickListener(this);

        registerUser = (Button)findViewById(R.id.btn_registeruser);
        registerUser.setOnClickListener(this);

        edtNickname = (EditText)findViewById(R.id.edt_nickName);
        edtEmail = (EditText)findViewById(R.id.edt_email);
        edtPassword = (EditText)findViewById(R.id.edt_password);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.txt_banner:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.btn_registeruser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        final String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        final String nickname = edtNickname.getText().toString().trim();

        if(nickname.isEmpty()) {
            edtNickname.setError("닉네임을 입력해주세요");
            edtNickname.requestFocus(); //필드에 초점 맞추기
            return;
        }

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
            edtPassword.setError("비밀번호를 8글자 이상 입력하세요.");
            edtPassword.requestFocus();
            return;
        }

        //progressBar 세팅하기
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        User user = new User(nickname, email);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterUserActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterUserActivity.this, LoginActivity.class));
                                }else
                                    Toast.makeText(RegisterUserActivity.this, "회원가입 실패", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }

}
