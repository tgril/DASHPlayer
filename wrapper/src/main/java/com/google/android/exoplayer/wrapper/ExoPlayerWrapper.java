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
    private DefaultTrackSelector trackSelector;

    public ExoPlayerWrapper(Context context) {
        this.context = context;
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this.context, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory("ExoPlayerWrapper", bandwidthMeter);
    }

    public void openUri(Uri uri) {
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this.context, trackSelector, new DefaultLoadControl());
        player.addListener(this);


        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        DataSource.Factory dataSourceFactory = buildDataSourceFactory(bandwidthMeter);
        MediaSource videoSource = new DashMediaSource(uri, buildDataSourceFactory(null),
                new DefaultDashChunkSource.Factory(dataSourceFactory), null, null);

        player.prepare(videoSource);
    }

    public void play() {
        player.setPlayWhenReady(true);
    }

    public void stop() {
        player.stop();
    }
}
