package com.drcdadaab.mrefugeeadmin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class EditNoticeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editTextEditNoticeTitle, editTextEditNoticeDescription;
    Button buttonEditNoticeExpiry;
    CheckBox checkBoxEditNoticeVisible;
    Spinner spinnerEditNoticeSource;
    String id, title, description, source, agencyLogo;
    int spinnerPosition;
    Long timestamp, expires;
    Boolean visible;
    ProgressDialog progress;

    int year, month, day;

    FirebaseDatabase db;
    DatabaseReference dbNotice, dbAgency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notice);

        editTextEditNoticeTitle = (EditText) findViewById(R.id.editTextEditNoticeTitle);
        editTextEditNoticeDescription = (EditText) findViewById(R.id.editTextEditNoticeDescription);
        buttonEditNoticeExpiry = (Button) findViewById(R.id.buttonEditNoticeExpiry);
        checkBoxEditNoticeVisible = (CheckBox) findViewById(R.id.checkBoxEditNoticeVisible);
        spinnerEditNoticeSource = (Spinner) findViewById(R.id.spinnerEditNoticeSource);

        progress = new ProgressDialog(this);
        progress.setMessage("Posting notice.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        db = FirebaseDatabase.getInstance();
        dbNotice = db.getReference("Notices");
        dbAgency = db.getReference("Agencies");


        Intent i = getIntent();
        id = i.getStringExtra("idKey");
        title = i.getStringExtra("titleKey");
        description = i.getStringExtra("descriptionKey");
        source = i.getStringExtra("sourceKey");
        agencyLogo = i.getStringExtra("logoKey");
        visible = i.getBooleanExtra("visibilityKey", true);


        dbAgency.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String agencyName = areaSnapshot.child("code").getValue(String.class);
                    areas.add(agencyName);
                }

                ArrayAdapter<String> agencyAdapter = new ArrayAdapter<String>(EditNoticeActivity.this, android.R.layout.simple_spinner_item, areas);
                agencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEditNoticeSource.setAdapter(agencyAdapter);
                spinnerEditNoticeSource.setOnItemSelectedListener(EditNoticeActivity.this);
                spinnerPosition = agencyAdapter.getPosition(source);
                spinnerEditNoticeSource.setSelection(spinnerPosition);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setTitle(title);
//
        editTextEditNoticeTitle.setText(title);
        checkBoxEditNoticeVisible.setChecked(visible);
        editTextEditNoticeDescription.setText(description);
//        spinnerEditNoticeSource.setSelection(spinnerPosition);
//        Picasso.with(getApplicationContext()).load(imageUrl).into(imageButtonNewsImage);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        source = parent.getItemAtPosition(pos).toString();
        getAgencyLogo(source);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void openDatePickerDialog(View v){
        showDialog(0);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;
            buttonEditNoticeExpiry.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
            GregorianCalendar gc = new GregorianCalendar();
            gc.clear();
            gc.set(year, month, day);
            expires = gc.getTimeInMillis()/1000;
        }
    };

    public void getAgencyLogo(String agencyName){

        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("Agencies").orderByChild("code").equalTo(agencyName);

        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    //TODO get the data here

                    agencyLogo = postSnapshot.child("imageUrl").getValue(String.class);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        query.addValueEventListener(valueEventListener);
    }

    public void editNotice(View v){
        title = editTextEditNoticeTitle.getText().toString().trim();
        description = editTextEditNoticeDescription.getText().toString().trim();
//        source = editTextNoticeSource.getText().toString().trim();
        if(checkBoxEditNoticeVisible.isChecked()){
            visible = true;
        }

        if(title.equals("") || description.equals("") || source.equals("")){
            Toast.makeText(EditNoticeActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.show();
//        id = dbNotice.push().getKey();
        timestamp = System.currentTimeMillis() * -1;

//        NoticeModel notice = new NoticeModel(id, title, description, source, "Admin", timestamp, expires, visible);
        NoticeModel notice = new NoticeModel(id, title, description, source, agencyLogo, "Admin", timestamp, expires, visible);
        try{
            dbNotice.child(id).setValue(notice);
            progress.dismiss();
            Toast.makeText(EditNoticeActivity.this, "Notice edited", Toast.LENGTH_SHORT).show();
        }
        catch (DatabaseException e){
            Toast.makeText(EditNoticeActivity.this, e+"", Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
//        startActivity(new Intent(EditNoticeActivity.this, ViewNoticeActivity.class));
        Intent noticeIntent = new Intent(EditNoticeActivity.this, ViewNoticeActivity.class);
        noticeIntent.putExtra("idKey", id);
        noticeIntent.putExtra("titleKey", title);
        noticeIntent.putExtra("sourceKey", source);
        noticeIntent.putExtra("timestampKey", timestamp);
        noticeIntent.putExtra("visibilityKey", visible);
        noticeIntent.putExtra("descriptionKey", description);
        noticeIntent.putExtra("logoKey", agencyLogo);
        startActivity(noticeIntent);
    }
}
