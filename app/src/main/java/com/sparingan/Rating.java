package com.sparingan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Rating extends AppCompatActivity {
private SeekBar pointBar;
private TextView givefeedback,givepoint;
private EditText feedback;
private String point,feedbackString,uid,partnerName;
private Button submit;
private ProgressBar load;
private FirebaseDatabase mInstance;
private DatabaseReference UsersRef;
private FirebaseAuth auth;
private static final String TAG = Rating.class.getSimpleName();
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        //get database reference from Users node
        mInstance = FirebaseDatabase.getInstance();
        UsersRef = mInstance.getReference("Users");

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getUid();
        pointBar = (SeekBar)findViewById(R.id.points);
        submit = (Button)findViewById(R.id.submit);
        givefeedback = (TextView)findViewById(R.id.givefeedback);
        givepoint = (TextView)findViewById(R.id.givepoints);
        feedback = (EditText)findViewById(R.id.feedback);
        load = (ProgressBar)findViewById(R.id.progRating);
        load.setVisibility(View.GONE);


        // Ambil Data Partner dari node "partner"
        UsersRef.child(uid).child("partner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Partner partner = dataSnapshot.getValue(Partner.class);
                    //Log.w(TAG,partner.dateP+partner.locationP+partner.sportP);
                    partnerName = partner.userP;
                }
                else {

                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        //Default Point
        point = "3";
        //SET progress Bar, get Value.
        pointBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                point = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            load.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            //CHECK KALAU SUDAH ADA NODE BELUM
            mInstance.getReference("Rating").child(partnerName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    while(dataSnapshot.child(String.valueOf(i)).exists()){
                        i++;
                    }

                   // mInstance.getReference("Rating").child(partnerName).child(String.valueOf(i)).child("feedback").setValue(feedbackString);
                    //mInstance.getReference("Rating").child(partnerName).child(String.valueOf(i)).child("point").setValue(point);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //TODO: CREATE NEW NODE FOR HISTORY
            feedbackString = feedback.getText().toString();
            mInstance.getReference("Users").child(uid).child("partner").removeValue();
            mInstance.getReference("Schedules").child(uid).removeValue();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            load.setVisibility(View.GONE);
            mInstance.getReference("Rating").child(partnerName).child(String.valueOf(i)).child("feedback").setValue(feedbackString);
            mInstance.getReference("Rating").child(partnerName).child(String.valueOf(i)).child("point").setValue(point);
            startActivity(new Intent(Rating.this,MainMenu.class));
            String sport = "0";
            String location = "0";
            String dateString = "0";
            UsersRef.child(uid).child("inPartner").setValue("0");
            Schedule schedule = new Schedule(sport,location,dateString);
            FirebaseDatabase.getInstance().getReference("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(schedule).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        finish();
                    }
                    else{

                    }
                }

                ;
            });

        }
    });
    }
}
