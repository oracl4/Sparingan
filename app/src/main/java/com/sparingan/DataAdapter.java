package com.sparingan;

import android.content.Context;
import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ArtistViewHolder> {

    private Context mCtx;
    private List<Data> dataList;
    private FirebaseDatabase mInstance;
    private DatabaseReference UsersRef;
    private String uid;
    private String userDate, userLocation, userSport, userUsername, inPartner,userLinkwa,userImage,userPhone;



    public DataAdapter(Context mCtx, List<Data> dataList) {
        this.mCtx = mCtx;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_artists, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {

        Data data = dataList.get(position);
        holder.textViewName.setText(data.username);
        String[] separated = String.valueOf(data.gabungan).split("_");
        holder.textViewSport.setText("Sport: " + separated[1]);
        holder.textViewDate.setText("Date:" + separated[2]);
        holder.textViewLocation.setText("Location: " + separated[3]);
        holder.btnpick.setVisibility(View.VISIBLE);
        holder.onClick(position);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class ArtistViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDate, textViewLocation, textViewSport;
        Button btnpick;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewLocation = itemView.findViewById(R.id.text_view_location);
            textViewSport = itemView.findViewById(R.id.text_view_sport);
            btnpick = (Button) itemView.findViewById(R.id.btn_pick);

        }

        public void onClick(final int position)

        {

            mInstance = FirebaseDatabase.getInstance();
            //get database reference from Users node
            UsersRef = mInstance.getReference("Users");
            //Text view to edit
            uid = FirebaseAuth.getInstance().getUid();


            btnpick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // MEMASUKKAN DATA PADA DIRI SENDIRI
                    Data data = dataList.get(position);
                    String[] separated2 = String.valueOf(data.gabungan).split("_");
                    UsersRef.child(uid).child("inPartner").setValue("1");
                    UsersRef.child(uid).child("gabungan").setValue("1"+"_"+separated2[1]+"_"+separated2[2]+"_"+separated2[3]);
                    UsersRef.child(uid).child("partner").child("userP").setValue(data.username);
                    UsersRef.child(uid).child("partner").child("sportP").setValue(separated2[1]);
                    UsersRef.child(uid).child("partner").child("locationP").setValue(separated2[3]);
                    UsersRef.child(uid).child("partner").child("dateP").setValue(separated2[2]);
                    UsersRef.child(uid).child("partner").child("linkwaP").setValue(data.linkwa);
                    UsersRef.child(uid).child("partner").child("phoneP").setValue(data.phone);
                    UsersRef.child(uid).child("partner").child("imageP").setValue(data.imageurl);
                    FirebaseDatabase.getInstance().getReference("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("date").setValue(separated2[2]);
                    FirebaseDatabase.getInstance().getReference("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("location").setValue(separated2[3]);
                    FirebaseDatabase.getInstance().getReference("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("sport").setValue(separated2[1]);

                    //MEMASUKKAN DATA PADA PARTNER

                    UsersRef.child(data.uid).child("inPartner").setValue("2");
                    UsersRef.child(data.uid).child("gabungan").setValue("2"+"_"+separated2[1]+"_"+separated2[2]+"_"+separated2[3]);
                    UsersRef.child(data.uid).child("partner").child("userP").setValue(userUsername);
                    UsersRef.child(data.uid).child("partner").child("sportP").setValue(separated2[1]);
                    UsersRef.child(data.uid).child("partner").child("locationP").setValue(separated2[3]);
                    UsersRef.child(data.uid).child("partner").child("dateP").setValue(separated2[2]);
                    UsersRef.child(data.uid).child("partner").child("linkwaP").setValue(userLinkwa);
                    UsersRef.child(data.uid).child("partner").child("phoneP").setValue(userPhone);
                    UsersRef.child(data.uid).child("partner").child("imageP").setValue(userImage);

                    Intent openThree = new Intent(mCtx,MainMenu.class);
                    mCtx.startActivity(openThree);


                }
            });

//===============================================================================================================
            // MENGAMBIL USERNAME, LINKWA, PHONE, IMAGE dari USER
            UsersRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        userUsername = user.username;
                        userPhone= user.phone;
                        userLinkwa=user.linkwa;
                        userImage=user.imageurl;

                        if (userUsername == null) {
                        } else {

                        }
                    } else {

                    }
                }

                @Override
                public void onCancelled(DatabaseError error){
                }
            });
//===============================================================================================================
            mInstance.getReference("Schedules").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        userDate = schedule.date;
                        userLocation = schedule.location;
                        userSport = schedule.sport;
                    } else {
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
//===============================================================================================================        }
    }

}}
