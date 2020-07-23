package com.example.robert.ph_prototype;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Nick on 3/7/2018.
 */

public class SplashActivity extends AppCompatActivity {

    /*
     * Splash Activity that redirects to the base activity. Advantages over using layout file is
     * that it prevents loading a blank splash (due to the application not fully initializing). Does
     * not use setContentView() because the splash is being displayed from the app theme.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

}