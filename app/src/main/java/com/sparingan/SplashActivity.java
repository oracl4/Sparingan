package com.sparingan;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.github.ybq.android.spinkit.style.ThreeBounce;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME = 3000;
    //ProgressBar progressBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //progressBar=(ProgressBar)findViewById(R.id.SpinKit);
       // ThreeBounce ThreeBounce = new ThreeBounce();
        //progressBar.setIndeterminateDrawable(ThreeBounce);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivity.this, LoginScreen.class );
                startActivity(splashIntent);
                finish();
            }
        },SPLASH_TIME);
    }
}
