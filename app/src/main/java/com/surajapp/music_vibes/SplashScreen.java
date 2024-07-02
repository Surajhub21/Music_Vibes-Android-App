package com.surajapp.music_vibes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_toast_create);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.darkPink));


        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(4000);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };thread.start();
    }
}