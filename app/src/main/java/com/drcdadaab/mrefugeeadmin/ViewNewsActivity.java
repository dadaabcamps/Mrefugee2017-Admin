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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class ViewNewsActivity extends AppCompatActivity {

    TextView textViewShowNewsTitle, textViewShowNewsAuthor, textViewShowNewsVisibility, textViewShowNewsContent;
    RelativeTimeTextView textViewShowNewsTimestamp;
    String title, author, content, imageUrl, id;
    Long timestamp;
    Boolean visibility;
    ImageView imageViewShowNewsImage;
    ProgressDialog progress;


    FirebaseDatabase db;
    DatabaseReference dbNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        textViewShowNewsTitle = (TextView) findViewById(R.id.textViewShowNewsTitle);
        textViewShowNewsAuthor = (TextView) findViewById(R.id.textViewShowNewsAuthor);
        textViewShowNewsTimestamp = (RelativeTimeTextView) findViewById(R.id.textViewShowNewsTimestamp);
        textViewShowNewsVisibility = (TextView) findViewById(R.id.textViewShowNewsVisibility);
        textViewShowNewsContent = (TextView) findViewById(R.id.textViewShowNewsContent);
        imageViewShowNewsImage = (ImageView) findViewById(R.id.imageViewShowNewsImage);

        db = FirebaseDatabase.getInstance();
        dbNews = db.getReference("News");

        progress = new ProgressDialog(this);
        progress.setMessage("Posting article.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        Intent i = getIntent();

        id = i.getStringExtra("idKey");
        title = i.getStringExtra("titleKey");
        author = i.getStringExtra("authorKey");
        timestamp = i.getLongExtra("timestampKey", 12);
        visibility = i.getBooleanExtra("visibilityKey",true);
        content = i.getStringExtra("contentKey");
        imageUrl = i.getStringExtra("imageKey");

        getSupportActionBar().setTitle(title);

        textViewShowNewsTitle.setText(title);
        textViewShowNewsAuthor.setText(author);
        textViewShowNewsTimestamp.setReferenceTime(new Date((timestamp * -1)).getTime());
        textViewShowNewsVisibility.setText(visibility.toString());
        textViewShowNewsContent.setText(content);
        Picasso.with(getApplicationContext()).load(imageUrl).into(imageViewShowNewsImage);

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
                Intent newsIntent = new Intent(ViewNewsActivity.this, EditNewsActivity.class);

                newsIntent.putExtra("idKey", id);
                newsIntent.putExtra("titleKey", title);
                newsIntent.putExtra("authorKey", author);
                newsIntent.putExtra("visibilityKey", visibility);
                newsIntent.putExtra("contentKey", content);
                newsIntent.putExtra("imageKey", imageUrl);

                startActivity(newsIntent);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_delete:

                try{
                    dbNews.child(id).removeValue();
                    progress.dismiss();
                    Toast.makeText(ViewNewsActivity.this, "News deleted", Toast.LENGTH_SHORT).show();
                }
                catch (DatabaseException e){
                    Toast.makeText(ViewNewsActivity.this, e+"", Toast.LENGTH_SHORT).show();
                }
                finish();
                startActivity(new Intent(ViewNewsActivity.this, ListNewsActivity.class));
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
