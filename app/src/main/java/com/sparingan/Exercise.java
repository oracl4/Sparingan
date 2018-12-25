package com.sparingan;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class Exercise extends AppCompatActivity {
private Button finish;
private TextView partnerInfo;
private FirebaseDatabase mInstance;
private DatabaseReference UsersRef;
private String partnerSport,partnerDate, partnerWA, partnerLocation,partnerName,partnerPhone;
private String uid;
private FirebaseAuth auth;

private static final String TAG = Exercise.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        mInstance = FirebaseDatabase.getInstance();
        //get database reference from Users node
        UsersRef = mInstance.getReference("Users");
        //Text view to edit
        auth = FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getUid();
        Log.w(TAG,uid);
        //partnerInfo.setText(auth.getUid());
        partnerInfo = (TextView) findViewById(R.id.userinfo);
        // Ambil Data Partner dari node "partner"
        UsersRef.child(uid).child("partner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Partner partner = dataSnapshot.getValue(Partner.class);
                    //Log.w(TAG,partner.dateP+partner.locationP+partner.sportP);
                    partnerInfo.setText("Partner : "+ partner.userP +"\n Sport : " + partner.sportP + "\n Location : " + partner.locationP);
                    //Tambahin lagi
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
    }

}
