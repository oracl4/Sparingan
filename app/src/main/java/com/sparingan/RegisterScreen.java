package com.sparingan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterScreen extends AppCompatActivity {
 private EditText inputEmail,inputPassword,inputUsername,inputPhone;
 private Button btnSignUp,btnBack;
 private FirebaseAuth auth;
 private String uid;
 private FirebaseDatabase mInstance;
 private DatabaseReference UsersRef;
 private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = findViewById(R.id.loading2);
        progressBar.setVisibility(View.GONE);
        auth =  FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.register_button);
        inputUsername = findViewById(R.id.username_register);
        inputEmail = findViewById(R.id.email_forgot_et);
        inputPhone = findViewById(R.id.phone_num);
        inputPassword = findViewById(R.id.password_register);
        btnBack = findViewById(R.id.back_to_login);
        //Mengatur behaviour dari Register button (BtnSignUp) dan Already have login button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = inputUsername.getText().toString();
                final String email = inputEmail.getText().toString().trim();
                final String phone = inputPhone.getText().toString();
                String password = inputPassword.getText().toString().trim();
                final String whatsapp = getString(R.string.linkwa)+phone+"&text=Yuk%20Sparring";

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //menyimpan username kedalam database (Method createUser)
                //TODO : ADD WHATSAPP AS CONTACT FEATURE USING WHATSAPP API
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); //agar tidak ada interaksi saat loading
                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterScreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                User user = new User(username,email,phone,whatsapp);
                                // Create new node in Database named "Users"
                               FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                           progressBar.setVisibility(View.GONE);
                                           Toast.makeText(RegisterScreen.this,getString(R.string.regist_success),Toast.LENGTH_LONG).show(); //TODO: Ganti ke dialog box
                                           String sport = "0";
                                           String location = "0";
                                           String dateString = "0";
                                           mInstance = FirebaseDatabase.getInstance();
                                           //get database reference from Users node
                                           UsersRef = mInstance.getReference("Users");
                                           //Text view to edit
                                           uid = FirebaseAuth.getInstance().getUid();
                                           UsersRef.child(uid).child("inPartner").setValue("0");
                                           UsersRef.child(uid).child("gabungan").setValue("0"+"_"+"0"+"_"+"0"+"_"+"0");
                                           UsersRef.child(uid).child("uid").setValue(uid);
                                           UsersRef.child(uid).child("imageurl").setValue("nopic");

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
                                   }
                               });

                               }
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterScreen.this, getString(R.string.regist_fail),
                                            Toast.LENGTH_LONG).show();
                                } else {

                                    startActivity(new Intent(RegisterScreen.this, LoginScreen.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }


}
