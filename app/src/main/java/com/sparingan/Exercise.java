package com.sparingan;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Exercise extends AppCompatActivity {
private Button finish;
private TextView partnerInfo;
private ImageView partnerpic;
private ImageButton whatsappbtn;
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
        //get database reference from Users node
        mInstance = FirebaseDatabase.getInstance();
        UsersRef = mInstance.getReference("Users");

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getUid();

        Log.w(TAG,uid);
        //partnerInfo.setText(auth.getUid());
        //INIT xml objects
        whatsappbtn = (ImageButton)findViewById(R.id.whatsappB);
        finish = (Button) findViewById(R.id.finish);
        partnerInfo = (TextView) findViewById(R.id.userinfo);
        partnerpic = (ImageView)findViewById(R.id.partnerpic);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: KASIH POPUP ARE YOU SURE LANG
                startActivity(new Intent(Exercise.this,Rating.class));
            }
        });

        UsersRef.child(uid).child("partner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Partner partner = dataSnapshot.getValue(Partner.class);
                    if(partner.imageP.equals("nopic")){

                    }else {
                        String url = partner.imageP;
                        Picasso.get().load(url).into(partnerpic);
                        Log.w(TAG,url);
                    }
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Ambil Data Partner dari node "partner"
        UsersRef.child(uid).child("partner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Partner partner = dataSnapshot.getValue(Partner.class);
                    //Log.w(TAG,partner.dateP+partner.locationP+partner.sportP);
                    partnerInfo.setText("Partner : "+ partner.userP +"\n Sport : " + partner.sportP + "\n Location : " + partner.locationP);
                    //Tambahin lagi
                    partnerWA = partner.linkwaP;
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
        whatsappbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(partnerWA));
                startActivity(intent);
            }
        });

    }
    @Override
    public void onStart(){
        super.onStart();
        UsersRef.child(uid).child("partner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    finish();
                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
