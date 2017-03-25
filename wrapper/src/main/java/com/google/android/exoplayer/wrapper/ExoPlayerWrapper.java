package com.google.android.exoplayer.wrapper;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;


/**
 * Created by tgril on 25.3.2017..
 */


public class ExoPlayerWrapper implements ExoPlayer.EventListener {
    @Override
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

    Context context;

    public SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();


    private Handler mainHandler;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;

    public ExoPlayerWrapper(Context context) {
        this.context = context;

        mediaDataSourceFactory = buildDataSourceFactory(BANDWIDTH_METER);
        mainHandler = new Handler();

        userAgent = Util.getUserAgent(context, "ExoPlayerWrapper");

        initializePlayer();
    }

    private void initializePlayer() {
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this.context, trackSelector, new DefaultLoadControl());
        //player.addListener(this.context);

        Uri uri = Uri.parse("http://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7.8506521BFC350652163895D4C26DEE124209AA9E&key=ik0");
        //Uri uri = Uri.parse("http://46.23.86.207/video/smurfs/smurfs.ism/smurfs.mpd");
        //Uri uri = Uri.parse("http://html5demos.com/assets/dizzy.mp4");

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        DataSource.Factory dataSourceFactory = buildDataSourceFactory(bandwidthMeter);
        MediaSource videoSource = new DashMediaSource(uri, buildDataSourceFactory(null),
                new DefaultDashChunkSource.Factory(dataSourceFactory), null, null);
        player.setPlayWhenReady(true);
        player.prepare(videoSource);
    }

    protected String userAgent;

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this.context, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public void openUri() {

    }

    public void play() {
        player.setPlayWhenReady(true);
    }

    public void pause() {
        player.stop();
    }
}
