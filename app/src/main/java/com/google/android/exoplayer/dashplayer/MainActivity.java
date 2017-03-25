package com.google.android.exoplayer.dashplayer;

import android.net.Uri;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends Activity implements ExoPlayer.EventListener {

    public void onTimelineChanged(Timeline timeline, Object manifest) {

        int i = 0;
        i = i+1;
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        int i = 0;
        i = i+1;
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        int i = 0;
        i = i+1;
    }
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        int i = 0;
        i = i+1;
    }
    @Override
    public void onLoadingChanged(boolean isLoading) {
        int i = 0;
        i = i+1;
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    /*private DataSource.Factory mediaDataSourceFactory;

    Handler mainHandler = new Handler();
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

    LoadControl loadControl = new DefaultLoadControl();
*/
    //SimpleExoPlayer player =
     //       ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);

    private Handler mainHandler;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaDataSourceFactory = buildDataSourceFactory(BANDWIDTH_METER);
        mainHandler = new Handler();

        setContentView(R.layout.activity_main);
        userAgent = Util.getUserAgent(this, "DashPlayerDemo");

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();

        initializePlayer();
    }

    private void initializePlayer() {
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl());
        player.addListener(this);

        simpleExoPlayerView.setPlayer(player);
        player.setPlayWhenReady(true);


        Uri uri = Uri.parse("http://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7.8506521BFC350652163895D4C26DEE124209AA9E&key=ik0");
        //Uri uri = Uri.parse("http://46.23.86.207/video/smurfs/smurfs.ism/smurfs.mpd");
        //Uri uri = Uri.parse("http://html5demos.com/assets/dizzy.mp4");

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        DataSource.Factory dataSourceFactory = buildDataSourceFactory(bandwidthMeter);
        MediaSource videoSource = new DashMediaSource(uri,buildDataSourceFactory(null),
                new DefaultDashChunkSource.Factory(dataSourceFactory),null,null);
        player.prepare(videoSource);
    }

    protected String userAgent;

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }
    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }
}
