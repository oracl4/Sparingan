package com.sparingan;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CreateSchedule extends Activity implements OnItemSelectedListener{
    private Button btnBack,btnFi;
    private EditText date;
    private ProgressBar progressBar;
    private DatePickerDialog picker;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        btnBack = (Button) findViewById(R.id.btnBk);
        btnFi = (Button) findViewById(R.id.btnFind);
        progressBar = (ProgressBar)findViewById(R.id.progressBarSch);
        progressBar.setVisibility(View.GONE);
        date = (EditText) findViewById(R.id.date);
        date.setInputType(InputType.TYPE_NULL);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CreateSchedule.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        // Spinner element
        final Spinner sport = (Spinner) findViewById(R.id.sport);
        final Spinner lokasi = (Spinner) findViewById(R.id.lokasi);
        // Spinner click listener
        sport.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> sport_categories = new ArrayList<String>();
        sport_categories.add("Basketball");
        sport_categories.add("Futsal");
        sport_categories.add("Soccer");
        sport_categories.add("Badminton");
        sport_categories.add("Atlhetics");
        List<String> location = new ArrayList<String>();
        location.add("Jakarta Barat");
        location.add("Jakarta Utara");
        location.add("Jakarta Timur");
        location.add("Jakarta Selatan");
        location.add("Jakarta Pusat");
        location.add("Depok I");
        location.add("Depok II");
        location.add("Sekitar UI");
        // Creating adapter for spinner
        ArrayAdapter<String> adapterSport = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sport_categories);
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, location);
        // Drop down layout style - list view with radio button
        adapterSport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sport.setAdapter(adapterSport);
        lokasi.setAdapter(adapterLocation);

        final String[] sportString = new String[1];
        final String[] lokasiString = new String[1];
        sport.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                sportString[0] = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        lokasi.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                lokasiString[0] = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        btnFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String sport = Arrays.toString(sportString);
                String location = Arrays.toString(lokasiString);
                String dateString = date.getText().toString();
                Schedule schedule = new Schedule(sport,location,dateString);
                FirebaseDatabase.getInstance().getReference("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(schedule).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateSchedule.this, getString(R.string.find_success), Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CreateSchedule.this,getString(R.string.find_fail),Toast.LENGTH_LONG).show();
                        }
                    }

                    ;
                });
            }

        });



   /* @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }*/

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}