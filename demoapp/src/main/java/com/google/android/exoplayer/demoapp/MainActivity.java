package com.google.android.exoplayer.demoapp;

import android.app.Activity;
import android.os.Bundle;


import com.google.android.exoplayer.wrapper.ExoPlayerWrapper;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class MainActivity extends Activity {
    ExoPlayerWrapper wrapper;


    private SimpleExoPlayerView simpleExoPlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();

        wrapper = new ExoPlayerWrapper(this);

        simpleExoPlayerView.setPlayer(wrapper.player);

        wrapper.play();

        //wrapper.openUri();
        //wrapper.play();
        //wrapper.pause();
    }
}
