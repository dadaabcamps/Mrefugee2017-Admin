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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateNoticeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editTextNoticeTitle, editTextNoticeDescription;
    Button buttonNoticeExpiry;
    CheckBox checkBoxNoticeVisible;
    Spinner spinnerNoticeSource;
    String id, title, description, source, agencyLogo;
    Long timestamp, expires;
    Boolean visible = false;
    ProgressDialog progress;

    int year, month, day;

    FirebaseDatabase db;
    DatabaseReference dbNotice;
    DatabaseReference dbAgency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notice);

        editTextNoticeTitle = (EditText) findViewById(R.id.editTextNoticeTitle);
        editTextNoticeDescription = (EditText) findViewById(R.id.editTextNoticeDescription);
//        editTextNoticeSource = (EditText) findViewById(R.id.editTextNoticeSource);
        buttonNoticeExpiry = (Button) findViewById(R.id.buttonNoticeExpiry);
        checkBoxNoticeVisible = (CheckBox) findViewById(R.id.checkBoxNoticeVisible);
        spinnerNoticeSource = (Spinner) findViewById(R.id.spinnerNoticeSource);

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

                ArrayAdapter<String> agencyAdapter = new ArrayAdapter<String>(CreateNoticeActivity.this, android.R.layout.simple_spinner_item, areas);
                agencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerNoticeSource.setAdapter(agencyAdapter);
                spinnerNoticeSource.setOnItemSelectedListener(CreateNoticeActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createNotice(View v){
        title = editTextNoticeTitle.getText().toString().trim();
        description = editTextNoticeDescription.getText().toString().trim();
//        source = editTextNoticeSource.getText().toString().trim();
        if(checkBoxNoticeVisible.isChecked()){
            visible = true;
        }

        if(title.equals("") || description.equals("") || source.equals("")){
            Toast.makeText(CreateNoticeActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.show();
        id = dbNotice.push().getKey();
        timestamp = System.currentTimeMillis() * -1;

//        NoticeModel notice = new NoticeModel(id, title, description, source, "Admin", timestamp, expires, visible);
        NoticeModel notice = new NoticeModel(id, title, description, source, agencyLogo, "Admin", timestamp, expires, visible);
        try{
            dbNotice.child(id).setValue(notice);
            progress.dismiss();
            Toast.makeText(CreateNoticeActivity.this, "Notice created", Toast.LENGTH_SHORT).show();
        }
        catch (DatabaseException e){
            Toast.makeText(CreateNoticeActivity.this, e+"", Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
        startActivity(new Intent(CreateNoticeActivity.this, ListNoticeActivity.class));
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
            buttonNoticeExpiry.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
            GregorianCalendar gc = new GregorianCalendar();
            gc.clear();
            gc.set(year, month, day);
            expires = gc.getTimeInMillis()/1000;
        }
    };

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        source = parent.getItemAtPosition(pos).toString();
        getAgencyLogo(source);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

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

}
