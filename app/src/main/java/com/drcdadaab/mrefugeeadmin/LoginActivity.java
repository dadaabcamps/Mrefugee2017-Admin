package com.drcdadaab.mrefugeeadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

//    Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    EditText editTextEmail, editTextPassword;
    String email, password;
    ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            openDashboardActivity();
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBarLogin =  (ProgressBar) findViewById(R.id.progressBarLogin);
    }

    public void loginUser(View v){
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        if(email.equals("") || password.equals("")){
            Toast.makeText(LoginActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBarLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBarLogin.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                } else {
                    progressBarLogin.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void openRegistrationActivity(View v){
        finish();
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    public void openDashboardActivity(){
        finish();
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
    }
}
