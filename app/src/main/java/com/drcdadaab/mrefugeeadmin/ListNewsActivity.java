package com.drcdadaab.mrefugeeadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ListNewsActivity extends AppCompatActivity {
    private static final String TAG = ListNewsActivity.class.getSimpleName();
    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference db;
    FirebaseRecyclerAdapter<NewsModel, NewsViewHolder> firebasenewsRecycleAdapter;
    ProgressBar progressBarNewsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);


        //Initialize Firebase DB
        db = FirebaseDatabase.getInstance().getReference();


        //SETUP RECYCLER
        rv = (RecyclerView) findViewById(R.id.recyclerViewNewsList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);


        progressBarNewsList = (ProgressBar) findViewById(R.id.progressBarNewsList);
        progressBarNewsList.setVisibility(View.VISIBLE);

        firebasenewsRecycleAdapter = new FirebaseRecyclerAdapter<NewsModel, NewsViewHolder>(NewsModel.class, R.layout.news_list_item, NewsViewHolder.class, db.child("News").orderByChild("timestamp")) {
            @Override
            protected void populateViewHolder(NewsViewHolder viewHolder, final NewsModel model, final int position) {
                viewHolder.textViewNewsListTitle.setText(model.getTitle());
                viewHolder.textViewNewsListVisible.setText(model.getVisible().toString());
                viewHolder.textViewNewsListSince.setReferenceTime(new Date((model.getTimestamp() * -1)).getTime());
                progressBarNewsList.setVisibility(View.GONE);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openNewsDetailActivity(model.getId(), model.getTitle(), model.getAuthor(), model.getTimestamp(), model.getVisible(), model.getContent(), model.imageUrl);
                    }
                });
            }

            private void openNewsDetailActivity(String id, String title, String author, Long timestamp, Boolean visible, String content, String imageUrl) {
                Intent newsIntent = new Intent(ListNewsActivity.this, ViewNewsActivity.class);
                newsIntent.putExtra("idKey", id);
                newsIntent.putExtra("titleKey", title);
                newsIntent.putExtra("authorKey", author);
                newsIntent.putExtra("timestampKey", timestamp);
                newsIntent.putExtra("visibilityKey", visible);
                newsIntent.putExtra("contentKey", content);
                newsIntent.putExtra("imageKey", imageUrl);

                startActivity(newsIntent);
            }
        };
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(firebasenewsRecycleAdapter);

    }

    public void openCreateNewsActivity(View v){
        startActivity( new Intent(ListNewsActivity.this, CreateNewsActivity.class));
    }
}
