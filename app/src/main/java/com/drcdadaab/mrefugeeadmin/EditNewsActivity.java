package com.drcdadaab.mrefugeeadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;

import static com.drcdadaab.mrefugeeadmin.R.id.textViewShowNewsTitle;

public class EditNewsActivity extends AppCompatActivity {

    EditText editTextEditNewsTitle, editTextEditNewsContent;
    ImageButton imageButtonNewsImage;
    CheckBox checkBoxEditNewsVisible;
    String id, title, content, author, imageUrl;
    Long timestamp;
    Boolean visible;
//    Boolean visibility;

    ProgressDialog progress;

    FirebaseDatabase db;
    DatabaseReference dbNews;
    private StorageReference mStorage;


    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri = null;
    private Uri downloadUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        editTextEditNewsTitle = (EditText) findViewById(R.id.editTextEditNewsTitle);
        editTextEditNewsContent = (EditText) findViewById(R.id.editTextEditNewsContent);
        checkBoxEditNewsVisible = (CheckBox) findViewById(R.id.checkBoxEditNewsVisible);
        imageButtonNewsImage = (ImageButton) findViewById(R.id.imageButtonNewsImage);

        progress = new ProgressDialog(this);
        progress.setMessage("Updating article.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        db = FirebaseDatabase.getInstance();
        dbNews = db.getReference("News");

        mStorage = FirebaseStorage.getInstance().getReference();

        imageButtonNewsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        Intent i = getIntent();

        id = i.getStringExtra("idKey");
        title = i.getStringExtra("titleKey");
        author = i.getStringExtra("authorKey");
//        timestamp = i.getLongExtra("timestampKey", 12);
        visible = i.getBooleanExtra("visibilityKey",true);
        content = i.getStringExtra("contentKey");
        imageUrl = i.getStringExtra("imageKey");

//        visible = visibility;
//
        getSupportActionBar().setTitle(title);
//
        editTextEditNewsTitle.setText(title);
//        textViewShowNewsTimestamp.setReferenceTime(new Date((timestamp)).getTime());
        checkBoxEditNewsVisible.setChecked(visible);
        editTextEditNewsContent.setText(content);
        Picasso.with(getApplicationContext()).load(imageUrl).into(imageButtonNewsImage);

    }

    public void updateNews(View v){
        attemptNewsUpload();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imageButtonNewsImage.setImageURI(imageUri);

        }
    }

    private void attemptNewsUpload(){
        title = editTextEditNewsTitle.getText().toString().trim();
        content = editTextEditNewsContent.getText().toString().trim();
        if(checkBoxEditNewsVisible.isChecked()){
            visible = true;
        }
        if(title.equals("") || content.equals("")){
            Toast.makeText(EditNewsActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        if(imageUri != null){
            StorageReference filePath = mStorage.child("News_Images").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    //noinspection VisibleForTests
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    //To order news items in reverse order use negative timestamp
                    timestamp = System.currentTimeMillis() * -1;
                    NewsModel news = new NewsModel(id, title, content, "Admin", timestamp,visible, downloadUrl.toString());

                    try{
                        dbNews.child(id).setValue(news);
                        progress.dismiss();
                        Toast.makeText(EditNewsActivity.this, "News edited", Toast.LENGTH_SHORT).show();
                    }
                    catch (DatabaseException e){
                        Toast.makeText(EditNewsActivity.this, e+"", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent newsIntent = new Intent(EditNewsActivity.this, ViewNewsActivity.class);
                    newsIntent.putExtra("idKey", id);
                    newsIntent.putExtra("titleKey", title);
                    newsIntent.putExtra("authorKey", author);
                    newsIntent.putExtra("timestampKey", timestamp);
                    newsIntent.putExtra("visibilityKey", visible);
                    newsIntent.putExtra("contentKey", content);
                    newsIntent.putExtra("imageKey", downloadUrl.toString());
                    finish();
                    startActivity(newsIntent);

                }
            });
        } else{
            timestamp = System.currentTimeMillis() * -1;
            NewsModel news = new NewsModel(id, title, content, "Admin", timestamp,visible, imageUrl);

            try{
                dbNews.child(id).setValue(news);
                progress.dismiss();
                Toast.makeText(EditNewsActivity.this, "News edited", Toast.LENGTH_SHORT).show();
            }
            catch (DatabaseException e){
                Toast.makeText(EditNewsActivity.this, e+"", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent newsIntent = new Intent(EditNewsActivity.this, ViewNewsActivity.class);
            newsIntent.putExtra("idKey", id);
            newsIntent.putExtra("titleKey", title);
            newsIntent.putExtra("authorKey", author);
            newsIntent.putExtra("timestampKey", timestamp);
            newsIntent.putExtra("visibilityKey", visible);
            newsIntent.putExtra("contentKey", content);
            newsIntent.putExtra("imageKey", imageUrl);

            finish();
            startActivity(newsIntent);

        }
    }
}
