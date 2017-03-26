package com.google.android.exoplayer.demoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import com.google.android.exoplayer.wrapper.ExoPlayerWrapper;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.Locale;

public class PlayerActivity extends Activity implements View.OnClickListener, DialogInterface.OnClickListener {

    private ExoPlayerWrapper playerWrapper;

    // customized ExoPlayer library view
    private SimpleExoPlayerView simpleExoPlayerView;

    // ExoPlayer default logger of content info
    private TextView debugTextView;
    private DebugTextViewHelper debugViewHelper;

    // bitrate selection dialog objects
    private CheckedTextView defaultView;
    private CheckedTextView[][] trackViews;
    private Integer groupIndex = null;
    private Integer trackIndex = null;

    // playback resume objects
    private int resumeWindow;
    private long resumePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        Button btnChangeBitrate = (Button) findViewById(R.id.change_bitrate);
        // assign listener to button for changing profile
        btnChangeBitrate.setOnClickListener(this);

        debugTextView = (TextView) findViewById(R.id.debug_text_view);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();
    }

    private void updateResumePosition() {
        resumeWindow = playerWrapper.getPlayer().getCurrentWindowIndex();
        resumePosition = playerWrapper.getPlayer().isCurrentWindowSeekable() ? Math.max(0, playerWrapper.getPlayer().getCurrentPosition()) : C.TIME_UNSET;
    }

    private void initializePlayer() {
        Bundle extras = getIntent().getExtras();
        String url = extras.getString("uri");
        Uri uri = Uri.parse(url);

        // custom implementation based on SimpleExoPlayer
        playerWrapper = new ExoPlayerWrapper(this);

        playerWrapper.openUri(uri, resumeWindow, resumePosition);
        playerWrapper.play();

        // assign player to layout
        simpleExoPlayerView.setPlayer(playerWrapper.getPlayer());

        // show stream info on top of the screen (use ExoPlayer library component)
        debugViewHelper = new DebugTextViewHelper(playerWrapper.getPlayer(), debugTextView);
        debugViewHelper.start();
    }

    private void releasePlayer() {
        if (playerWrapper != null) {
            debugViewHelper.stop();
            debugViewHelper = null;
            updateResumePosition();
            playerWrapper.getPlayer().release();
            playerWrapper = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }



    // raise dialog for profile selection
    public void showSelectionDialog(Activity activity, CharSequence title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setView(buildView(builder.getContext()))
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }


    // view builder for dialog
    private View buildView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.track_selection_dialog, null);
        ViewGroup root = (ViewGroup) view.findViewById(R.id.root);

        // option for adaptive streaming (one element)
        defaultView = (CheckedTextView) inflater.inflate(android.R.layout.simple_list_item_single_choice, root, false);
        defaultView.setText("Adaptive");
        defaultView.setFocusable(true);
        defaultView.setOnClickListener(this);
        root.addView(inflater.inflate(R.layout.list_divider, root, false));
        root.addView(defaultView);

        int length = playerWrapper.getTrackGroups().length;
        trackViews = new CheckedTextView[length][];

        // iterate through all track groups and tracks and create one object per group and track
        for (int groupIndex = 0; groupIndex < length; groupIndex++) {
            TrackGroup group = playerWrapper.getTrackGroups().get(groupIndex);
            trackViews[groupIndex] = new CheckedTextView[group.length];
            for (int trackIndex = 0; trackIndex < group.length; trackIndex++) {
                if (trackIndex == 0) {
                    root.addView(inflater.inflate(R.layout.list_divider, root, false));
                }

                int trackViewLayoutId = android.R.layout.simple_list_item_single_choice;
                CheckedTextView trackView = (CheckedTextView) inflater.inflate(
                        trackViewLayoutId, root, false);
                trackView.setText(buildTrackName(group.getFormat(trackIndex)));
                trackView.setFocusable(true);
                trackView.setTag(Pair.create(groupIndex, trackIndex));
                trackView.setOnClickListener(this);

                trackViews[groupIndex][trackIndex] = trackView;
                root.addView(trackView);
            }
        }

        // global variables containg dialog selection
        if (groupIndex != null && trackIndex != null)
            trackViews[groupIndex][trackIndex].setChecked(true);
        else
            defaultView.setChecked(true);

        return view;
    }

    // DialogInterface.OnClickListener
    @Override
    public void onClick(DialogInterface dialog, int which) {
        // start playback of new track/s
        playerWrapper.applyTrackSelection(this.groupIndex, this.trackIndex);
    }

    // View.OnClickListener
    @Override
    public void onClick(View view) {
        // pressed button for changing bitrate
        if (view.getId() == R.id.change_bitrate) {
            showSelectionDialog(this, ((Button) view).getText());
        } else {
            resetSelection();
            // tapped on first item in list (adaptive streaming)
            if (view == defaultView) {
                groupIndex = null;
                trackIndex = null;
                defaultView.setChecked(true);
            } else {
                // tapped on any other item in the list (single profile)
                Pair<Integer, Integer> tag = (Pair<Integer, Integer>) view.getTag();
                groupIndex = tag.first;
                trackIndex = tag.second;
                trackViews[groupIndex][trackIndex].setChecked(true);
            }
        }
    }


    // clear selection of each list element
    private void resetSelection() {
        defaultView.setChecked(false);
        for (int i = 0; i < trackViews.length; i++) {
            for (int j = 0; j < trackViews[i].length; j++) {
                trackViews[i][j].setChecked(false);
            }
        }
    }

    // Track name construction.

    private static String buildTrackName(Format format) {
        String trackName = joinWithSeparator(buildResolutionString(format), buildBitrateString(format));
        return trackName.length() == 0 ? "unknown" : trackName;
    }

    private static String buildResolutionString(Format format) {
        return format.width == Format.NO_VALUE || format.height == Format.NO_VALUE
                ? "" : format.width + "x" + format.height;
    }

    private static String buildBitrateString(Format format) {
        return format.bitrate == Format.NO_VALUE ? ""
                : String.format(Locale.US, "%.2fMbit", format.bitrate / 1000000f);
    }

    private static String joinWithSeparator(String first, String second) {
        return first.length() == 0 ? second : (second.length() == 0 ? first : first + ", " + second);
    }
}
