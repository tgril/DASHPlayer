package com.google.android.exoplayer.demoapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;


import com.google.android.exoplayer.wrapper.ExoPlayerWrapper;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class PlayerActivity extends Activity {

    ExoPlayerWrapper wrapper;
    private SimpleExoPlayerView simpleExoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String url = extras.getString("uri");
        Uri uri = Uri.parse(url);

        setContentView(R.layout.activity_player);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();

        wrapper = new ExoPlayerWrapper(this);

        wrapper.openUri(uri);
        wrapper.play();

        simpleExoPlayerView.setPlayer(wrapper.player);
    }
}
