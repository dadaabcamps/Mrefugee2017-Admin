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

public class ViewAgencyActivity extends AppCompatActivity {
    TextView textViewShowAgencyName, textViewShowAgencyCode;
    String name, code, imageUrl, id;
    ImageView imageViewShowAgencyLogo;
    ProgressDialog progress;


    FirebaseDatabase db;
    DatabaseReference dbAgency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_agency);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        textViewShowAgencyName = (TextView) findViewById(R.id.textViewShowAgencyName);
        textViewShowAgencyCode = (TextView) findViewById(R.id.textViewShowAgencyCode);
        imageViewShowAgencyLogo = (ImageView) findViewById(R.id.imageViewShowAgencyLogo);

        db = FirebaseDatabase.getInstance();
        dbAgency = db.getReference("Agencies");

        progress = new ProgressDialog(this);
        progress.setMessage("Posting article.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        Intent i = getIntent();
        name = i.getStringExtra("nameKey");
        id = i.getStringExtra("idKey");
        code = i.getStringExtra("codeKey");
        imageUrl = i.getStringExtra("imageKey");

        getSupportActionBar().setTitle(name);
        textViewShowAgencyName.setText(name);
        textViewShowAgencyCode.setText(code);
        Picasso.with(getApplicationContext()).load(imageUrl).into(imageViewShowAgencyLogo);
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
                Intent agencyIntent = new Intent(ViewAgencyActivity.this, EditAgencyActivity.class);

                agencyIntent.putExtra("idKey", id);
                agencyIntent.putExtra("nameKey", name);
                agencyIntent.putExtra("codeKey", code);
                agencyIntent.putExtra("imageKey", imageUrl);

                startActivity(agencyIntent);
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_delete:

                try{
                    dbAgency.child(id).removeValue();
                    progress.dismiss();
                    Toast.makeText(ViewAgencyActivity.this, "Agency deleted", Toast.LENGTH_SHORT).show();
                }
                catch (DatabaseException e){
                    Toast.makeText(ViewAgencyActivity.this, e+"", Toast.LENGTH_SHORT).show();
                }
                finish();
                startActivity(new Intent(ViewAgencyActivity.this, ListAgencyActivity.class));
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
