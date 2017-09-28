package com.drcdadaab.mrefugeeadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ListAgencyActivity extends AppCompatActivity {
    private static final String TAG = ListNewsActivity.class.getSimpleName();
    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference db;
    FirebaseRecyclerAdapter<AgencyModel, AgencyViewHolder> firebasenewsRecycleAdapter;
    ProgressBar progressBarAgencyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_agency);


        //Initialize Firebase DB
        db = FirebaseDatabase.getInstance().getReference();


        //SETUP RECYCLER
        rv = (RecyclerView) findViewById(R.id.recyclerViewAgencyList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);


        progressBarAgencyList = (ProgressBar) findViewById(R.id.progressBarAgencyList);
        progressBarAgencyList.setVisibility(View.VISIBLE);

        firebasenewsRecycleAdapter = new FirebaseRecyclerAdapter<AgencyModel, AgencyViewHolder>(AgencyModel.class, R.layout.agency_list_item, AgencyViewHolder.class, db.child("Agencies")) {
            @Override
            protected void populateViewHolder(AgencyViewHolder viewHolder, final AgencyModel model, final int position) {
                viewHolder.textViewAgencyListName.setText(model.getName());
                progressBarAgencyList.setVisibility(View.GONE);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAgencyDetailActivity(model.getName(), model.getId(), model.getCode(), model.imageUrl);
                    }
                });
            }

            private void openAgencyDetailActivity(String name, String id, String code, String imageUrl) {
                Intent newsIntent = new Intent(ListAgencyActivity.this, ViewAgencyActivity.class);
                newsIntent.putExtra("nameKey", name);
                newsIntent.putExtra("idKey", id);
                newsIntent.putExtra("codeKey", code);
                newsIntent.putExtra("imageKey", imageUrl);
                startActivity(newsIntent);
            }
        };
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(firebasenewsRecycleAdapter);

    }

    public void openCreateAgencyActivity(View v){
        startActivity( new Intent(ListAgencyActivity.this, CreateAgencyActivity.class));
    }
}
