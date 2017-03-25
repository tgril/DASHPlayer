package com.google.android.exoplayer.demoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class StreamListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_list);

        startActivity(buildIntent(this));
    }

    public Intent buildIntent(Context context) {
        Intent intent = new Intent(context, PlayerActivity.class);

        String uri = "http://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7.8506521BFC350652163895D4C26DEE124209AA9E&key=ik0";
        //String uri = "http://46.23.86.207/video/smurfs/smurfs.ism/smurfs.mpd";
        //String uri = "http://html5demos.com/assets/dizzy.mp4";

        intent.putExtra("uri", uri);
        return intent;
    }
}
