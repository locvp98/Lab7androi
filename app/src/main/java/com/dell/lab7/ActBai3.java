package com.dell.lab7;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ActBai3 extends AppCompatActivity {
    private ImageView giay;
    private ImageView phut;
    private ImageView gio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_bai3);
        giay = findViewById(R.id.giay);
        phut = findViewById(R.id.phut);
        gio = findViewById(R.id.gio);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        giay.startAnimation(animation);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.phut);
        phut.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.gio);
        gio.startAnimation(animation2);
    }

}
