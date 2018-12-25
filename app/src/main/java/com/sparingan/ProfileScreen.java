package com.sparingan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileScreen extends AppCompatActivity {
private TextView profilename,profilephone,profileemail;
private FirebaseDatabase mInstance;
private DatabaseReference UsersRef;
//private ArrayList<String> allUsername = new ArrayList<>();
//private ArrayList<String> allPhone = new ArrayList<>();
//private ArrayList<String> allEmail = new ArrayList<>();
private String uid;
private String imageurl;
private FirebaseStorage storage;
private StorageReference storageReference;
private FirebaseAuth auth;
private Button change;
private ImageView profile_picture;
private Uri filePath;
private FirebaseAuth.AuthStateListener authListener;
private final int PICK_IMAGE_REQUEST = 71;
private static final String TAG = ProfileScreen.class.getSimpleName();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        //firebase auth init
        auth = FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getUid();
        //Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //init objects
        profile_picture = (ImageView) findViewById(R.id.profile_pic);
       change = (Button)findViewById(R.id.chg_pic);
        profilename = (TextView) findViewById(R.id.profile_name);
        profilephone = (TextView) findViewById(R.id.profile_phone);
        profileemail = (TextView) findViewById(R.id.profile_email);
        //firebase database init
        mInstance = FirebaseDatabase.getInstance();
        //get database reference from Users node
        UsersRef = mInstance.getReference("Users");

//GET PROFILE PIC IF EXISTS
            UsersRef.child(uid).child("profilepic").child("imageurl").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String url = dataSnapshot.getValue().toString();
                        Picasso.get().load(url).into(profile_picture);
                        Log.w(TAG,url);
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


       change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }

        });
        //GET CURRENT USER LOGGED IN
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(uid).getValue(User.class);
                //Log.w(TAG,user.username + user.email + "aha");
               profilename.setText(user.username);
               profilephone.setText(user.phone);
               profileemail.setText(user.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Error, data can't be received");
            }

        });
    }




    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
    private void uploadImage() {
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + uid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                          @Override
                                                                          public void onSuccess(Uri uri) {//MENGAMBIL URL DARI GAMBAR
                                                                              Uri downloadUrl = uri;
                                                                              Log.w(TAG, downloadUrl.toString());
                                                                              User user = new User(downloadUrl.toString());
                                                                              FirebaseDatabase.getInstance().getReference("Users").child(uid).child("profilepic").setValue(user);
                                                                              //Do what you want with the url
                                                                          }
                                                                      });
                            Toast.makeText(ProfileScreen.this, "Successfully updated your profile picture!", Toast.LENGTH_SHORT).show();




                            // User user = new User(taskSnapshot.getUploadSessionUri().toString());
                        }

                    }) .addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileScreen.this, "Upload Failed...", Toast.LENGTH_SHORT).show();

                }
            }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
        }
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,requestCode,data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            uploadImage();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                profile_picture.setImageBitmap(bitmap);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
  /* UNTUK MENGARAHKAN KE PROFILE ORANG LAIN
  /* public void collectUsername (Map<String,Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allUsername.add((String) singleUser.get("username"));
        }
        //Arrays.toString(allUsername.toArray());

    }
    public void collectPhone (Map<String,Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allUsername.add((String) singleUser.get("phone"));
        }
        //Arrays.toString(allUsername.toArray());

    }
    public void collectEmail (Map<String,Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allUsername.add((String) singleUser.get("phone"));
        }
        //Arrays.toString(allUsername.toArray());

    }*/
}
