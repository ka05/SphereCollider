package com.example.clay.spherecollider.view.lib;

/**
 * Created by Clay on 5/16/2015.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.clay.spherecollider.R;
import com.example.clay.spherecollider.SphereCollider;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(Build.VERSION.SDK_INT == 21){
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(getString(R.string.app_name), BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher), Color.parseColor("#34495e"));
            this.setTaskDescription(taskDesc);
        }

        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;

        Drawable ballDrawable = findViewById(R.id.ball).getBackground();
        Drawable deflaterDrawable = findViewById(R.id.deflater).getBackground();
        Drawable inflaterDrawable = findViewById(R.id.inflater).getBackground();

        ballDrawable.setColorFilter(Color.parseColor("#ff7935"), mode);
        deflaterDrawable.setColorFilter(Color.parseColor("#dd2ea8"), mode);
        inflaterDrawable.setColorFilter(Color.parseColor("#2dd2d7"), mode);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, SphereCollider.class);
                startActivity(i);

                // close this activity
                finish();

                //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
            }
        }, SPLASH_TIME_OUT);
    }

}