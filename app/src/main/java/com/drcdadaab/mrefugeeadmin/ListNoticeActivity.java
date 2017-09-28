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

public class ListNoticeActivity extends AppCompatActivity {

    private static final String TAG = ListNoticeActivity.class.getSimpleName();
    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference db;
    FirebaseRecyclerAdapter<NoticeModel, NoticeViewHolder> firebasenewsRecycleAdapter;
    ProgressBar progressBarNoticeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notice);


        //Initialize Firebase DB
        db = FirebaseDatabase.getInstance().getReference();

        //SETUP RECYCLER
        rv = (RecyclerView) findViewById(R.id.recyclerViewNoticeList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);


        progressBarNoticeList = (ProgressBar) findViewById(R.id.progressBarNoticeList);
        progressBarNoticeList.setVisibility(View.VISIBLE);

        firebasenewsRecycleAdapter = new FirebaseRecyclerAdapter<NoticeModel, NoticeViewHolder>(NoticeModel.class, R.layout.notice_list_item, NoticeViewHolder.class, db.child("Notices").orderByChild("timestamp")) {
            @Override
            protected void populateViewHolder(NoticeViewHolder viewHolder, final NoticeModel model, final int position) {
                viewHolder.textViewNoticeListTitle.setText(model.getTitle());
                viewHolder.textViewNoticeListVisible.setText(model.getVisible().toString());
                viewHolder.textViewNoticeListSince.setReferenceTime(new Date((model.getTimestamp() * -1)).getTime());
                progressBarNoticeList.setVisibility(View.GONE);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //firebasenewsRecycleAdapter.getRef(position).removeValue();
                        openNoticeDetailActivity(model.getId(), model.getTitle(), model.getSource(), model.getTimestamp(), model.getVisible(), model.getDescription(), model.getAgencyLogo());
                    }
                });
            }

            private void openNoticeDetailActivity(String id, String title, String source, Long timestamp, Boolean visible, String description, String agencyLogo) {
                Intent newsIntent = new Intent(ListNoticeActivity.this, ViewNoticeActivity.class);
                newsIntent.putExtra("idKey", id);
                newsIntent.putExtra("titleKey", title);
                newsIntent.putExtra("sourceKey", source);
                newsIntent.putExtra("timestampKey", timestamp);
                newsIntent.putExtra("visibilityKey", visible);
                newsIntent.putExtra("descriptionKey", description);
                newsIntent.putExtra("logoKey", agencyLogo);

                startActivity(newsIntent);
            }
        };
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(firebasenewsRecycleAdapter);

    }

    public void openCreateNoticeActivity(View v){
        startActivity( new Intent(ListNoticeActivity.this, CreateNoticeActivity.class));
    }
}
