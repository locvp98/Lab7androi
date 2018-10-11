package com.dell.lab7;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ActBai2 extends AppCompatActivity {
    private ImageView aniview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_bai2);
        aniview = findViewById(R.id.aniview);

    }

    public void xulyall(View view) {
        showimg("All");
    }


    public void xulyDoRemon(View view) {
        showimg("Doremon");
    }

    public void xulynobita(View view) {
        showimg("nobita");
    }

    private void showimg(String img) {

        //hide
        ObjectAnimator anim=ObjectAnimator.ofFloat(aniview,"translationX",0f,500f);
        anim.setDuration(2000);
        ObjectAnimator anim1=ObjectAnimator.ofFloat(aniview,"alpha",1f,0f);
        anim1.setDuration(2000);

        //show

        ObjectAnimator anim2=ObjectAnimator.ofFloat(aniview,"translationX",-500f,0f);
        anim2.setDuration(2000);
        ObjectAnimator anim3=ObjectAnimator.ofFloat(aniview,"alpha",0f,1f);
        anim3.setDuration(2000);

        //config
        AnimatorSet asn=new AnimatorSet();
        asn.play(anim2).with(anim3).after(anim).after(anim1);
        asn.start();

        final String imgname=img;

        anim1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (imgname=="All"){
                    aniview.setImageResource(R.drawable.all);
                }
                if (imgname=="nobita"){
                    aniview.setImageResource(R.drawable.nobita);
                }
                if (imgname=="Doremon"){
                    aniview.setImageResource(R.drawable.doraemon);
                }
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });




    }
}
