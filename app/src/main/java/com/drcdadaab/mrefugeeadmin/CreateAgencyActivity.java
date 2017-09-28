package com.drcdadaab.mrefugeeadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class CreateAgencyActivity extends AppCompatActivity {
    EditText editTextAgencyName;
    EditText editTextAgencyCode;
    ImageButton imageButtonAgencyLogo;
    ProgressDialog progress;


    FirebaseDatabase db;
    DatabaseReference dbAgency;
    private StorageReference mStorage;

    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri = null;
    private Uri downloadUrl;
    String id, name, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_agency);

        editTextAgencyName = (EditText) findViewById(R.id.editTextAgencyName);
        editTextAgencyCode = (EditText) findViewById(R.id.editTextAgencyCode);
        imageButtonAgencyLogo = (ImageButton) findViewById(R.id.imageButtonAgencyLogo);

        progress = new ProgressDialog(this);
        progress.setMessage("Saving agency.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        db = FirebaseDatabase.getInstance();
        dbAgency = db.getReference("Agencies");
        mStorage = FirebaseStorage.getInstance().getReference();

        imageButtonAgencyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }

    public void createNews(View v){
        attemptAgencyUpload();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imageButtonAgencyLogo.setImageURI(imageUri);

        }
    }

    private void attemptAgencyUpload(){
        name = editTextAgencyName.getText().toString().trim();
        code = editTextAgencyCode.getText().toString().trim();
        if(name.equals("") || code.equals("") || imageUri == null){
            Toast.makeText(CreateAgencyActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.show();
        StorageReference filePath = mStorage.child("Agency_Logos").child(imageUri.getLastPathSegment());
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                //noinspection VisibleForTests
                downloadUrl = taskSnapshot.getDownloadUrl();
                id = dbAgency.push().getKey();
                AgencyModel agency = new AgencyModel(id, name, code, downloadUrl.toString());

                try{
                    dbAgency.child(id).setValue(agency);
                    progress.dismiss();
                    Toast.makeText(CreateAgencyActivity.this, "Agency saved", Toast.LENGTH_SHORT).show();
                }
                catch (DatabaseException e){
                    Toast.makeText(CreateAgencyActivity.this, e+"", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
                startActivity(new Intent(CreateAgencyActivity.this, ListAgencyActivity.class));
            }
        });

    }
}
