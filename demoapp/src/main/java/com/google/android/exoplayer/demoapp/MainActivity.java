package com.google.android.exoplayer.demoapp;

import android.app.Activity;
import android.net.Uri;
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

        Uri uri = Uri.parse("http://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7.8506521BFC350652163895D4C26DEE124209AA9E&key=ik0");
        //Uri uri = Uri.parse("http://46.23.86.207/video/smurfs/smurfs.ism/smurfs.mpd");
        //Uri uri = Uri.parse("http://html5demos.com/assets/dizzy.mp4");

        wrapper.openUri(uri);
        wrapper.play();

        simpleExoPlayerView.setPlayer(wrapper.player);
    }
}
