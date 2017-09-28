package com.drcdadaab.mrefugeeadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateNewsActivity extends AppCompatActivity {

    EditText editTextCreateNewsTitle, editTextCreateNewsContent;
    ImageButton imageButtonNewsImage;
    CheckBox checkBoxCreateNewsVisible;
    String id, title, content;
    Long timestamp;
    Boolean visible = false;

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
        setContentView(R.layout.activity_create_news);

        editTextCreateNewsTitle = (EditText) findViewById(R.id.editTextCreateNewsTitle);
        editTextCreateNewsContent = (EditText) findViewById(R.id.editTextCreateNewsContent);
        checkBoxCreateNewsVisible = (CheckBox) findViewById(R.id.checkBoxCreateNewsVisible);
        imageButtonNewsImage = (ImageButton) findViewById(R.id.imageButtonNewsImage);

        progress = new ProgressDialog(this);
        progress.setMessage("Posting article.. ");
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
    }

    public void createNews(View v){
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
        title = editTextCreateNewsTitle.getText().toString().trim();
        content = editTextCreateNewsContent.getText().toString().trim();
        if(checkBoxCreateNewsVisible.isChecked()){
            visible = true;
        }
        if(title.equals("") || content.equals("") || imageUri == null){
            Toast.makeText(CreateNewsActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        StorageReference filePath = mStorage.child("News_Images").child(imageUri.getLastPathSegment());
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                //noinspection VisibleForTests
                downloadUrl = taskSnapshot.getDownloadUrl();
                id = dbNews.push().getKey();
                //To order news items in reverse order use negative timestamp
                timestamp = System.currentTimeMillis() * -1;
                NewsModel news = new NewsModel(id, title, content, "Admin", timestamp,visible, downloadUrl.toString());

                try{
                    dbNews.child(id).setValue(news);
                    progress.dismiss();
                    Toast.makeText(CreateNewsActivity.this, "News created", Toast.LENGTH_SHORT).show();
                }
                catch (DatabaseException e){
                    Toast.makeText(CreateNewsActivity.this, e+"", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
                startActivity(new Intent(CreateNewsActivity.this, ListNewsActivity.class));
            }
        });


    }
}
