package com.pratyusha.myemp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
                // Start your app main activity
                //Redirect to Splash Activity to our main Activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, 4000);
    }
}