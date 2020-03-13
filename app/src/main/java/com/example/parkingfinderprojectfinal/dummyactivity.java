package com.example.parkingfinderprojectfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class dummyactivity extends AppCompatActivity {
    public  static int splash_time_out=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummyactivity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(dummyactivity.this,home.class);
                startActivity(home);
                finish();
            }
        },splash_time_out);
    }
}
