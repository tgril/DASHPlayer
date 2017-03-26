package com.google.android.exoplayer.wrapper;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;


/**
 * Created by tgril on 25.3.2017..
 */


public class ExoPlayerWrapper {

    private static final TrackSelection.Factory FIXED_FACTORY = new FixedTrackSelection.Factory();
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private MappingTrackSelector.SelectionOverride override;
    private DefaultTrackSelector trackSelector;

    private Context context;
    private SimpleExoPlayer player;


    public ExoPlayerWrapper(Context context) {
        this.context = context;
    }

    public SimpleExoPlayer getPlayer() {
        return this.player;
    }

    public void applyTrackSelection(Integer groupIndex, Integer trackIndex) {
        if (groupIndex == null || trackIndex == null) {
            override = null;
        } else  {
            override = new MappingTrackSelector.SelectionOverride(FIXED_FACTORY, groupIndex, trackIndex);
        }

        int rendererIndex = getVideoRendererIndex();
        if (override != null) {
            trackSelector.setSelectionOverride(rendererIndex, trackSelector.getCurrentMappedTrackInfo().getTrackGroups(rendererIndex), override);
        } else {
            trackSelector.clearSelectionOverrides(getVideoRendererIndex());
        }
    }

    public TrackGroupArray getTrackGroups() {
        return trackSelector.getCurrentMappedTrackInfo().getTrackGroups(getVideoRendererIndex());
    }

    public void openUri(Uri uri) {
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, new DefaultLoadControl());


        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        DataSource.Factory dataSourceFactory = buildDataSourceFactory(bandwidthMeter);
        MediaSource videoSource = new DashMediaSource(uri, buildDataSourceFactory(null),
                new DefaultDashChunkSource.Factory(dataSourceFactory), null, null);
        LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);

        player.prepare(loopingSource);


    }

    public void play() {
        player.setPlayWhenReady(true);
    }

    public void stop() {
        player.stop();
        player.release();
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    private DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(context, bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory("ExoPlayerWrapper", bandwidthMeter);
    }

    private int getVideoRendererIndex() {
        for (int i = 0; i < player.getRendererCount(); i++) {
            if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO) {
                return i;
            }
        }
        return 0;
    }
}
