package com.dell.lab7;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class AcyBai1 extends AppCompatActivity {
    private ImageView aniview;
    private Button zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acy_bai1);

        aniview=findViewById(R.id.aniview);
        zoom=findViewById(R.id.zoom);

    }

    public void xulyrotation(View view) {
       int dest=360;
       if (aniview.getRotation()==360){
           System.out.println(aniview.getAlpha());
           dest=0;
       }
        ObjectAnimator animator1=ObjectAnimator.ofFloat(aniview,"rotation",dest);
       animator1.setDuration(2000);
       animator1.start();

    }

    public void xulymoving(View view) {
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moving);
                animation1.setRepeatCount(2);
        aniview.startAnimation(animation1);
    }

    public void xulyzoom(View view) {
        @SuppressLint("ResourceType")
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.zoom);
        aniview.startAnimation(animation);
    }
}
