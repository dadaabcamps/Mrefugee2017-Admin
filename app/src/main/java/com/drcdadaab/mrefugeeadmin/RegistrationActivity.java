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

public class RegistrationActivity extends AppCompatActivity {
//    Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

//    EditText Variables
    EditText editTextRegisterEmail, editTextRegisterPassword;
    String email, password;
    ProgressBar progressBarRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null ){
            openDashboardActivity();
        }


        editTextRegisterEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
        progressBarRegister = (ProgressBar) findViewById(R.id.progressBarRegister);

    }

    public void openLoginActivity(View v){
        finish();
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    public void registerUser(View v){
        email = editTextRegisterEmail.getText().toString().trim();
        password = editTextRegisterPassword.getText().toString().trim();

        if (email.equals("") || password.equals("")) {
            Toast.makeText(RegistrationActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBarRegister.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    progressBarRegister.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(RegistrationActivity.this, DashboardActivity.class));
                } else {
                    progressBarRegister.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegistrationActivity.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void openDashboardActivity(){
        finish();
        startActivity(new Intent(RegistrationActivity.this, DashboardActivity.class));
    }
}
