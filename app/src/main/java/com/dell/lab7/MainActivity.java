package com.dell.lab7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void xulybai1(View view) {
        startActivity(new Intent(MainActivity.this,AcyBai1.class));
    }

    public void xulybai2(View view) {
        startActivity(new Intent(MainActivity.this,ActBai2.class));


    }
    public void xulybai3(View view) {
        startActivity(new Intent(MainActivity.this,ActBai3.class));

    }
}
