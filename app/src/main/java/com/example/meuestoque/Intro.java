package com.example.meuestoque;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);



        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent it_main = new Intent(Intro.this,MainActivity.class);
                it_main.putExtra("cont",1);
                startActivity(it_main);
                finish();
            }
        }, 3000);

    }
}