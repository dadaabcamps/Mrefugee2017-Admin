package com.drcdadaab.mrefugeeadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ViewNoticeActivity extends AppCompatActivity {

    TextView textViewShowNoticeTitle, textViewShowNoticeSource, textViewShowNoticeVisibility, textViewShowNoticeDescription;
    RelativeTimeTextView textViewShowNoticeTimestamp;
    String id, title, source, description, agencyLogo;
    Long timestamp;
    Boolean visibility;
    ProgressDialog progress;


    FirebaseDatabase db;
    DatabaseReference dbNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notice);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        textViewShowNoticeTitle = (TextView) findViewById(R.id.textViewShowNoticeTitle);
        textViewShowNoticeSource = (TextView) findViewById(R.id.textViewShowNoticeSource);
        textViewShowNoticeTimestamp = (RelativeTimeTextView) findViewById(R.id.textViewShowNoticeTimestamp);
        textViewShowNoticeVisibility = (TextView) findViewById(R.id.textViewShowNoticeVisibility);
        textViewShowNoticeDescription = (TextView) findViewById(R.id.textViewShowNoticeDescription);

        db = FirebaseDatabase.getInstance();
        dbNotice = db.getReference("Notices");

        progress = new ProgressDialog(this);
        progress.setMessage("Posting article.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        Intent i = getIntent();

        id = i.getStringExtra("idKey");
        title = i.getStringExtra("titleKey");
        source = i.getStringExtra("sourceKey");
        timestamp = i.getLongExtra("timestampKey", 12);
        visibility = i.getBooleanExtra("visibilityKey",true);
        description = i.getStringExtra("descriptionKey");
        agencyLogo = i.getStringExtra("logoKey");

        getSupportActionBar().setTitle(title);

        textViewShowNoticeTitle.setText(title);
        textViewShowNoticeSource.setText(source);
        textViewShowNoticeTimestamp.setReferenceTime(new Date((timestamp * -1)).getTime());
        textViewShowNoticeVisibility.setText(visibility.toString());
        textViewShowNoticeDescription.setText(description);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crud_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent noticeIntent = new Intent(ViewNoticeActivity.this, EditNoticeActivity.class);

                noticeIntent.putExtra("idKey", id);
                noticeIntent.putExtra("titleKey", title);
                noticeIntent.putExtra("sourceKey", source);
                noticeIntent.putExtra("visibilityKey", visibility);
                noticeIntent.putExtra("descriptionKey", description);
                noticeIntent.putExtra("logoKey", agencyLogo);

                startActivity(noticeIntent);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_delete:

                try{
                    dbNotice.child(id).removeValue();
                    progress.dismiss();
                    Toast.makeText(ViewNoticeActivity.this, "Notice deleted", Toast.LENGTH_SHORT).show();
                }
                catch (DatabaseException e){
                    Toast.makeText(ViewNoticeActivity.this, e+"", Toast.LENGTH_SHORT).show();
                }
                finish();
                startActivity(new Intent(ViewNoticeActivity.this, ListNoticeActivity.class));
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
