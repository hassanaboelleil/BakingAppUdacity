package com.example.hassan.bakingappfinal.fragment;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hassan.bakingappfinal.R;
import com.example.hassan.bakingappfinal.model.ModelStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static com.example.hassan.bakingappfinal.activity.Step.PANES;
import static com.example.hassan.bakingappfinal.activity.Step.POSITION;
import static com.example.hassan.bakingappfinal.adapter.AdapterStep.STEPS;

public class IngredientStepDetailFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener {

    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private boolean autoPlay = true;
    private int currentWindow;
    private long playbackPosition;
    private TrackSelector trackSelector;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private TextView description;
    private Button previousBtn;
    private Button nextBtn;
    private ArrayList<ModelStep> stepArrayList;
    private int index;
    private int mPosition;
    private boolean twoPane;


    private static long position = 0;

    //              constructor
    public IngredientStepDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            stepArrayList = savedInstanceState.getParcelableArrayList(STEPS);
            twoPane = savedInstanceState.getBoolean(PANES);
            mPosition = savedInstanceState.getInt(POSITION);
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, true);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        description = (TextView) rootView.findViewById(R.id.tv_description);
        playerView = (SimpleExoPlayerView) rootView.findViewById(R.id.sepv_step_video);
        previousBtn = (Button) rootView.findViewById(R.id.bt_previous);
        nextBtn = (Button) rootView.findViewById(R.id.bt_next);

        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        mPosition = getArguments().getInt(POSITION);
        index = mPosition ;
        twoPane = getArguments().getBoolean(PANES);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (twoPane) {
            showStepsForTab();
        } else {
            showStepsForPhone();
        }
    }



    public void showStepsForTab() {
        playerView.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        stepArrayList = getArguments().getParcelableArrayList(STEPS);
        assert stepArrayList != null;
        description.setText(stepArrayList.get(index).getDescription());
        playStepVideo(index);
    }


    private void playStepVideo(int index) {
        playerView.setVisibility(View.VISIBLE);
        playerView.requestFocus();
        String videoUrl = stepArrayList.get(index).getVideoUrl();
        String thumbNailUrl = stepArrayList.get(index).getThumbnailUrl();
        if (!videoUrl.isEmpty()) {
            initializePlayer(Uri.parse(videoUrl));
        } else if (!thumbNailUrl.isEmpty()) {
            initializePlayer(Uri.parse(thumbNailUrl));
        } else {
            playerView.setVisibility(View.GONE);
        }
    }

    void isLandscape() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            hideSystemUi();
    }

    private void showStepsForPhone() {
        showStepsForTab();
        isLandscape();
        previousBtn.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        exoPlayer = null;
        // Create an instance of the ExoPlayer.

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(),
                null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);

        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

        // Set the ExoPlayer.EventListener to this activity.
        exoPlayer.addListener(this);

        playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);

//        exoPlayer.seekTo(currentWindow, playbackPosition);

        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(getActivity(), "Baking App");

        MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                new DefaultDataSourceFactory(getActivity(), BANDWIDTH_METER,
                        new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER)),
                new DefaultExtractorsFactory(), null, null);

        exoPlayer.prepare(mediaSource);
        restExoPlayer(position, false);
    }

    private void restExoPlayer(long position, boolean playWhenReady) {
        this.position = position;
        exoPlayer.seekTo(position);
        exoPlayer.setPlayWhenReady(playWhenReady);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                if (index < stepArrayList.size() - 1) {
                    int index = ++this.index;
                    description.setText(stepArrayList.get(index).getDescription());
                    restExoPlayer(0, false);
                    playStepVideo(index);
                } else {
                    Toast.makeText(getActivity(), R.string.end_of_steps, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_previous:
                if (index > 0) {
                    int index = --this.index;
                    description.setText(stepArrayList.get(index).getDescription());
                    restExoPlayer(0, false);
                    playStepVideo(index);
                } else {
                    Toast.makeText(getActivity(), R.string.start_of_steps, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (exoPlayer != null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
        outState.putParcelableArrayList(STEPS, stepArrayList);
        outState.putBoolean(PANES, twoPane);
        outState.putInt(POSITION, mPosition);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            Toast.makeText(getActivity(), "Playing", Toast.LENGTH_LONG).show();
        } else if ((playbackState == ExoPlayer.STATE_READY)) {

        }

        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            position = exoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            Toast.makeText(getActivity(), errorString, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }


}
