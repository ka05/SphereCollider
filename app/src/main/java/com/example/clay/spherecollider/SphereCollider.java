package com.example.clay.spherecollider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.clay.spherecollider.view.dialogs.CustomModal;
import com.example.clay.spherecollider.view.level.LevelView;

import java.util.HashMap;


public class SphereCollider extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
//        overridePendingTransition(R.anim.splashfadeout, R.anim.mainfadein);
        setContentView(R.layout.activity_sphere_collider);
        initSplashButtons();
        initBG();
    }

    /**
     * sets up background colors for spheres
     */
    public void initBG(){

        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        Drawable ballDrawable = findViewById(R.id.ball).getBackground();
        Drawable deflaterDrawable = findViewById(R.id.deflater).getBackground();
        Drawable inflaterDrawable = findViewById(R.id.inflater).getBackground();

        ballDrawable.setColorFilter(Color.parseColor("#ff7935"), mode);
        deflaterDrawable.setColorFilter(Color.parseColor("#dd2ea8"), mode);
        inflaterDrawable.setColorFilter(Color.parseColor("#2dd2d7"), mode);
    }


    /**
     * sets up listeners for splash buttons
     */
    public void initSplashButtons(){

        // push to levels activity
        findViewById(R.id.btnViewLevels).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showLevels();
            }

        });

        // about button
        findViewById(R.id.btnViewAbout).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HashMap<String, String> options = new HashMap<String, String>();
                // options:
                options.put("modal_title", "ABOUT THIS APP");
                options.put("modal_content", getString(R.string.about_info));
                CustomModal cm = new CustomModal(SphereCollider.this, "info", options);
            }

        });

        // how to play button
        findViewById(R.id.btnHelp).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HashMap<String, String> options = new HashMap<String, String>();
                // options:
                options.put("modal_title", "HOW TO PLAY");
                options.put("modal_content", getString(R.string.help_info));
                CustomModal cm = new CustomModal(SphereCollider.this, "info", options);
            }

        });

    }

    public void showLevels(){
        // create and Intent to launch the ViewContact Activity
        Intent viewLevels = new Intent(SphereCollider.this, LevelView.class);
        startActivity(viewLevels);
//        overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
    }
}
