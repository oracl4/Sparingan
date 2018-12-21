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
import com.kaopiz.kprogresshud.KProgressHUD;

public class LoginScreen extends AppCompatActivity {

    private EditText inputEmail,inputPassword;
    private FirebaseAuth auth;
    private Button btnLogin,btnSignUp,btnForgot;
    private KProgressHUD hud;
   // private ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
       if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginScreen.this, MainMenu.class));
            finish();
        } /*Kalau User sudah Login sebelumnya, langsung ke main menu */
        setContentView(R.layout.activity_login);
       // Lib untuk loading dialog
      hud =KProgressHUD.create(LoginScreen.this)
              .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
              .setLabel("Please wait")
              .setDetailsLabel("Signing")
              .setCancellable(true)
              .setAnimationSpeed(2)
              .setDimAmount(0.8f);
        btnLogin = (Button) findViewById(R.id.login_button);
        btnSignUp = (Button) findViewById(R.id.go_to_reg_button);
        inputEmail = (EditText) findViewById(R.id.email_forgot_et);
        inputPassword = (EditText) findViewById(R.id.password_login);
        btnForgot = (Button) findViewById(R.id.go_to_forgot);
        auth = FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this, RegisterScreen.class));
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                startActivity(new Intent(LoginScreen.this,ForgotPassword.class));
            }

        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                hud.show();
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    hud.dismiss();
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginScreen.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    hud.dismiss();
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    Intent intent = new Intent(LoginScreen.this, MainMenu.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });
            }
        });/* Untuk pindah activity ke Register Screen, this->RegisterScreen*/
}
    }
