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
import com.squareup.picasso.Picasso;

public class EditAgencyActivity extends AppCompatActivity {
    EditText editTextEditAgencyName;
    EditText editTextEditAgencyCode;
    ImageButton imageButtonEditAgencyLogo;
    ProgressDialog progress;


    FirebaseDatabase db;
    DatabaseReference dbAgency;
    private StorageReference mStorage;

    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri = null;
    private Uri downloadUrl;
    String id, name, code, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_agency);

        editTextEditAgencyName = (EditText) findViewById(R.id.editTextEditAgencyName);
        editTextEditAgencyCode = (EditText) findViewById(R.id.editTextEditAgencyCode);
        imageButtonEditAgencyLogo = (ImageButton) findViewById(R.id.imageButtonEditAgencyLogo);

        progress = new ProgressDialog(this);
        progress.setMessage("Updating agency.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        db = FirebaseDatabase.getInstance();
        dbAgency = db.getReference("Agencies");
        mStorage = FirebaseStorage.getInstance().getReference();

        imageButtonEditAgencyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        Intent i = getIntent();
        name = i.getStringExtra("nameKey");
        id = i.getStringExtra("idKey");
        code = i.getStringExtra("codeKey");
        imageUrl = i.getStringExtra("imageKey");

        getSupportActionBar().setTitle(name);
        editTextEditAgencyName.setText(name);
        editTextEditAgencyCode.setText(code);
        Picasso.with(getApplicationContext()).load(imageUrl).into(imageButtonEditAgencyLogo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imageButtonEditAgencyLogo.setImageURI(imageUri);

        }
    }

    public void editAgency(View v){
        attemptAgencyUpload();
    }

    private void attemptAgencyUpload(){
        name = editTextEditAgencyName.getText().toString().trim();
        code = editTextEditAgencyCode.getText().toString().trim();
        if(name.equals("") || code.equals("")){
            Toast.makeText(EditAgencyActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.show();

        if (imageUri != null){
            StorageReference filePath = mStorage.child("Agency_Logos").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    //noinspection VisibleForTests
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    AgencyModel agency = new AgencyModel(id, name, code, downloadUrl.toString());

                    try{
                        dbAgency.child(id).setValue(agency);
                        progress.dismiss();
                        Toast.makeText(EditAgencyActivity.this, "Agency edited", Toast.LENGTH_SHORT).show();
                    }
                    catch (DatabaseException e){
                        Toast.makeText(EditAgencyActivity.this, e+"", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    startActivity(new Intent(EditAgencyActivity.this, ViewAgencyActivity.class));
                    Intent agencyIntent = new Intent(EditAgencyActivity.this, ViewAgencyActivity.class);
                    agencyIntent.putExtra("nameKey", name);
                    agencyIntent.putExtra("idKey", id);
                    agencyIntent.putExtra("codeKey", code);
                    agencyIntent.putExtra("imageKey", downloadUrl.toString());
                    finish();
                    startActivity(agencyIntent);
                }
            });
        } else{
            AgencyModel agency = new AgencyModel(id, name, code, imageUrl);

            try{
                dbAgency.child(id).setValue(agency);
                progress.dismiss();
                Toast.makeText(EditAgencyActivity.this, "Agency edited", Toast.LENGTH_SHORT).show();
            }
            catch (DatabaseException e){
                Toast.makeText(EditAgencyActivity.this, e+"", Toast.LENGTH_SHORT).show();
                return;
            }
//                    startActivity(new Intent(EditAgencyActivity.this, ViewAgencyActivity.class));
            Intent agencyIntent = new Intent(EditAgencyActivity.this, ViewAgencyActivity.class);
            agencyIntent.putExtra("nameKey", name);
            agencyIntent.putExtra("idKey", id);
            agencyIntent.putExtra("codeKey", code);
            agencyIntent.putExtra("imageKey", imageUrl);
            finish();
            startActivity(agencyIntent);
        }


    }
}
