package com.sparingan;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int i=0;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private Button findButton;
    private TextView welcome,test;
    private String uid;
    private FirebaseDatabase mInstance;
    private DatabaseReference UsersRef;
    private String userDate,userLocation,userSport,userUsername;
    private ArrayList<String> allLocation = new ArrayList<>();
    private ArrayList<String> allDate = new ArrayList<>();
    private ArrayList<String> allSport = new ArrayList<>();
    private ArrayList<String> allUsername = new ArrayList<>();
    private ArrayList<String> allWA = new ArrayList<>();
    private ArrayList<String> allPhone = new ArrayList<>();
    private static final String TAG = MainMenu.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        findButton = (Button) findViewById(R.id.findpartner);
        test = (TextView)findViewById(R.id.Text);
        //get Database Instance
        mInstance = FirebaseDatabase.getInstance();
        //get database reference from Users node
        UsersRef = mInstance.getReference("Users");
        //Text view to edit
        uid = FirebaseAuth.getInstance().getUid();
        welcome = (TextView)findViewById(R.id.usernameText);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
      //  final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Show Welcome text in main menu
        UsersRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);

                        welcome.setText("Welcome , " + user.username + " ! ");
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
        mInstance.getReference().child("Users").child(uid)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                User user = dataSnapshot.getValue(User.class);
                                userUsername = user.username;
                                }
                                else{

                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "Failed to read value.");
                            }
                        });
            mInstance.getReference("Schedules").child(uid).addValueEventListener(new ValueEventListener() { //collect logged in user's allDate,allLocation, and allSport
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        userDate = schedule.date;
                        userLocation = schedule.location;
                        userSport = schedule.sport;
                    }
                    else {
                        test.setText("data failed to get");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

//TODO untuk match data masih belum berhasil

        mInstance.getReference().child("Users")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                collectUsername((Map<String, Object>) dataSnapshot.getValue());
                                collectWA((Map<String, Object>) dataSnapshot.getValue());
                                collectPhone((Map<String, Object>) dataSnapshot.getValue());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "Failed to read value.");
                            }
                        });

        mInstance.getReference().child("Schedules")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(uid).exists()){ //in case user haven't made a schedule

                                    //Get map of users in datasnapshot
                                 collectDate((Map<String, Object>) dataSnapshot.getValue());
                                collectLocation((Map<String, Object>) dataSnapshot.getValue());
                                collectSport((Map<String, Object>) dataSnapshot.getValue());
                                //DELETE LOGGED IN USER'S DATAS IN THE ARRAY
                                allDate.remove(userDate);
                                allLocation.remove(userLocation);
                                allSport.remove(userSport);
                                allUsername.remove(userUsername);
                                test.setText("No match found yet...");

                                //COMPARE USER'S SCHEDULE VALUES TO ALL EXISTING SCHEDULE
                                for (i = 0; i < allDate.size(); i++) {
                                    if (userDate.equals(allDate.get(i)) && userSport.equals(allSport.get(i)) && userLocation.equals(allLocation.get(i))) {
                                        test.setText("MATCH FOUND with " + allUsername.get(i) +"!");
                                        UsersRef.child(uid).child("partner").child("userP").setValue(allUsername.get(i));
                                        UsersRef.child(uid).child("partner").child("linkwaP").setValue(allWA.get(i));
                                        UsersRef.child(uid).child("partner").child("phoneP").setValue(allPhone.get(i));
                                        break;
                                    }

                                    test.setText("No match found yet...");

                                }
                            } else{
                                    test.setText("No schedule have been made.");
                                }
                                i=0;
                        }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "Failed to read value.");
                            }
                        });



        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainMenu.this, LoginScreen.class));
                    finish();
                }}};

        findButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainMenu.this, CreateSchedule.class));
            }

        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(MainMenu.this, ProfileScreen.class));
            // Handle the camera action
        } else if (id == R.id.nav_signout) {
            signOut();
        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void signOut(){
        auth.signOut();
    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void collectDate (Map<String,Object> users) { //COLLECT ALL DATES



        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allDate.add((String) singleUser.get("date"));
        }


    }
    public void collectSport (Map<String,Object> users) { //COLLECT ALL SPORTS



        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allSport.add((String) singleUser.get("sport"));
        }


    }
    public void collectLocation (Map<String,Object> users) {



        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allLocation.add((String) singleUser.get("location"));
        }


    }
    public void collectUsername (Map<String,Object> users) {



        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allUsername.add((String) singleUser.get("username"));
        }
        //Arrays.toString(allUsername.toArray());

    }
    public void collectWA (Map<String,Object> users) {



        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allWA.add((String) singleUser.get("linkwa"));
        }
        //Arrays.toString(allUsername.toArray());

    }
    public void collectPhone (Map<String,Object> users) {



        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allPhone.add((String) singleUser.get("phone"));
        }
        //Arrays.toString(allUsername.toArray());

    }
}