package com.example.nearme;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearme.pojos.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private EditText et_email,et_password;
    private Button btn_login;
    private TextView tv_createAcc;
    private FirebaseAuth mAuth;

    private ProgressBar loginProgress_bar;


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            Intent intent = new Intent(Login.this,Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.etEmail);
        et_password = findViewById(R.id.etPassword);

        btn_login = findViewById(R.id.btnLogin);
        tv_createAcc = findViewById(R.id.tvCreateAccount);
        loginProgress_bar = findViewById(R.id.login_progressbar);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
                //startActivity(new Intent(getApplicationContext(),Dashboard.class));
            }
        });

        tv_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Registration.class));
            }
        });
    }

    private void userLogin() {
        String password = et_password.getText().toString().trim();
        String email = et_email.getText().toString().trim();

        if (email.isEmpty()) {
            et_email.setError(getString(R.string.emptyField));
            et_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError(getString(R.string.invalidEmail));
            et_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            et_password.setError(getString(R.string.emptyField));
            et_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            et_password.setError(getString(R.string.invalidLength));
            et_password.requestFocus();
            return;
        }

        loginProgress_bar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginProgress_bar.setVisibility(View.GONE);
                    finish();
                    Intent intent = new Intent(Login.this,Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    loginProgress_bar.setVisibility(View.GONE);
                }
            }
        });

    }
}
