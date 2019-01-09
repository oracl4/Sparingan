package com.sparingan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private List<Data> dataList;
    private TextView noData;
    private Button btnok,btnpick;
    private String uid,uid2;
    private FirebaseDatabase mInstance;
    private DatabaseReference UsersRef;



    DatabaseReference dbArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnok = (Button) findViewById(R.id.button_ok);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new DataAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        noData = (TextView) findViewById(R.id.no_data);
        noData.setVisibility(View.GONE);
        mInstance = FirebaseDatabase.getInstance();
        //get database reference from Users node
        UsersRef = mInstance.getReference("Users");
        //Text view to edit
        uid = FirebaseAuth.getInstance().getUid();

        final Spinner sport = (Spinner) findViewById(R.id.sport);


        List<String> sport_categories = new ArrayList<String>();
        sport_categories.add("Basketball");
        sport_categories.add("Futsal");
        sport_categories.add("Soccer");
        sport_categories.add("Badminton");
        sport_categories.add("Atlhetics");

        ArrayAdapter<String> adapterSport = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sport_categories);
        adapterSport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sport.setAdapter(adapterSport);
        final String[] sportString = new String[1];
        sport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // progress.setVisibility(View.VISIBLE);
                String sport = Arrays.toString(sportString);
                //String location = Arrays.toString(lokasiString);
                //String dateString = date.getText().toString();

                //Schedule schedule = new Schedule(sport,location,dateString);
                Query query7 = FirebaseDatabase.getInstance().getReference("Users")
                        .orderByChild("gabungan")
                        .startAt("0" + "_" + sport)
                        .endAt("0" + "_" + sport + "\uf8ff");


                query7.addListenerForSingleValueEvent(valueEventListener);


                // FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("gabungan").setValue(gabungan);


            }

        });



    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dataList.clear();
            if (dataSnapshot.exists()) {
                noData.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data data = snapshot.getValue(Data.class);
                    if(uid.equals(data.uid)){
                    }
                    else{
                     dataList.add(data);
                    }

                }
                adapter.notifyDataSetChanged();
            }else{

               noData.setVisibility(View.VISIBLE);



            }
        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
