package com.drcdadaab.mrefugeeadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity {

//    Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    static boolean calledAlready = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if( currentUser == null){
            openLoginActivity();
        }

        if(!calledAlready){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
    }


    public void openLoginActivity(){
        finish();
        startActivity( new Intent(DashboardActivity.this, LoginActivity.class));
    }

    public void logOut(View v){
        mAuth.signOut();
        openLoginActivity();
    }

//    public void openCreateNewsActivity(View v){
//        startActivity( new Intent(DashboardActivity.this, CreateNewsActivity.class));
//    }
//
//    public void openCreateNoticeActivity(View v){
//        startActivity( new Intent(DashboardActivity.this, CreateNoticeActivity.class));
//    }
    public void openListNewsActivity(View v){
        startActivity( new Intent(DashboardActivity.this, ListNewsActivity.class));
    }
    public void openListNoticeActivity(View v){
        startActivity( new Intent(DashboardActivity.this, ListNoticeActivity.class));
    }
    public void openListAgencyActivity(View v){
        startActivity( new Intent(DashboardActivity.this, ListAgencyActivity.class));
    }
}
