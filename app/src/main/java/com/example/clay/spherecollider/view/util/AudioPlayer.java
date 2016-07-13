package com.example.clay.spherecollider.view.util;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Clay on 4/25/2015.
 */
public class AudioPlayer {
    MediaPlayer mp;

    public AudioPlayer(Context context){

    }

    public void startSound(Context context, String soundType){
        switch (soundType){
            case "coin10":
//                mp = MediaPlayer.create(context, R.raw.coin10);
                break;
            case "coin1":
//                mp = MediaPlayer.create(context, R.raw.coin1);
                break;
            case "coinBad":
//                mp = MediaPlayer.create(context, R.raw.beep2);
                break;
        }

//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mp.release();
//            }
//        });
        mp.start();
    }

    public void die(){
        mp.release();
    }
}
