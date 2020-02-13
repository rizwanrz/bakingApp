package com.example.bakingapp.rizwan.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.pojo.Steps;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static timber.log.Timber.i;

public class VideoFragment extends Fragment {

    private static final String TAG = VideoFragment.class.getSimpleName();

    public VideoFragment() {}

    public ArrayList<Steps> stepsArrayList;
    Steps stepClicked;
    SimpleExoPlayer mExoPlayer;
    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.thumbnail_url)
    ImageView thumbnailUrlImage;
    public int stepIndex;
    private long mPlayerPosition;
    String videoUrl;
    Uri videoUrl_Parse;
    Uri thumbnailUrl_Parse;
    String thumbnailUrl;
    @BindView(R.id.previous_button)
    Button previousButton;
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.step_long_description)
    TextView stepLongDescription;
    String stepLongDescriptionUrl;
    private boolean autoPlay = true;
    boolean mTwoPane;

    public static final String STEP_INSTRUCTIONS = "long_instructions";
    public static final String AUTO_PLAY_KEY = "autoplay";
    public static final String KEY_POSITION = "position";
    public static final String STEPS_LIST_INDEX = "list_index";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        ButterKnife.bind(this, rootView);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            stepClicked = getArguments().getParcelable("Steps");
            if (stepClicked != null) {
                mTwoPane = getArguments().getBoolean("TwoPane");
                stepIndex = getArguments().getInt("StepIndex");
                stepsArrayList = getArguments().getParcelableArrayList("StepsArrayList");

                if (stepsArrayList == null || stepsArrayList.size() == 0) {
                    stepsArrayList = new ArrayList<>();
                }

                videoUrl = stepClicked.getVideoUrl();
                i(TAG, "VideoUrl: %s", stepClicked.getVideoUrl());
                videoUrl_Parse = Uri.parse(videoUrl);

                thumbnailUrl = stepClicked.getThumbnailUrl();
                thumbnailUrl_Parse = Uri.parse(thumbnailUrl);

                checkButtonState(stepIndex);
                stepLongDescriptionUrl = stepClicked.getStepLongDescription();
                i(TAG, "Step: %s", stepClicked.getStepLongDescription());

                stepLongDescription.setText(stepLongDescriptionUrl);

                if (thumbnailUrl != null) {
                    Picasso.with(getContext())
                            .load(thumbnailUrl_Parse)
                            .into(thumbnailUrlImage);
                }
            }
            if (mTwoPane) {
                previousButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.INVISIBLE);

            } else {
                previousButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (stepIndex < stepsArrayList.size() - 1) {
                            stepIndex++;
                            stepClicked = stepsArrayList.get(stepIndex);
                            stepLongDescription.setText(stepClicked.getStepLongDescription());
                            videoUrl = stepClicked.getVideoUrl();
                            i("VideoUrlNext: %s", stepClicked.getVideoUrl());
                            videoUrl_Parse = Uri.parse(videoUrl);
                            mExoPlayer.release();
                            mExoPlayer = null;
                            initializePlayer(videoUrl_Parse);
                            checkButtonState(stepIndex);
                        }
                    }
                });

                previousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (stepIndex > 0) {
                            stepIndex--;
                            stepClicked = stepsArrayList.get(stepIndex);
                            stepLongDescription.setText(stepClicked.getStepLongDescription());
                            videoUrl = stepClicked.getVideoUrl();
                            i("VideoUrlPrevious: %s", stepClicked.getVideoUrl());
                            videoUrl_Parse = Uri.parse(videoUrl);
                            initializePlayer(videoUrl_Parse);
                            checkButtonState(stepIndex);
                        }
                    }
                });
            }
        }
        if (savedInstanceState != null) {
            stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_LIST_INDEX);
            mPlayerPosition = savedInstanceState.getLong(KEY_POSITION);
            autoPlay = savedInstanceState.getBoolean(AUTO_PLAY_KEY, true);
        }

        return rootView;
    }

    private void checkButtonState(int stepIndex) {

        if (stepIndex == 0) {
            previousButton.setEnabled(false);
            previousButton.setClickable(false);
            previousButton.setBackground(getActivity().getDrawable(R.color.light_gray));
        } else {
            previousButton.setEnabled(true);
            previousButton.setClickable(true);
            previousButton.setBackground(getActivity().getDrawable(R.color.colorSecondary));
        }

        if (stepIndex == stepsArrayList.size() - 1) {
            nextButton.setEnabled(false);
            nextButton.setClickable(false);
            nextButton.setBackground(getActivity().getDrawable(R.color.light_gray));
        } else {
            nextButton.setEnabled(true);
            nextButton.setClickable(true);
            nextButton.setBackground(getActivity().getDrawable(R.color.colorSecondary));
        }
    }

    public void initializePlayer(Uri videoUrl) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer((SimpleExoPlayer) mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(videoUrl,
                    new DefaultDataSourceFactory(Objects.requireNonNull(getContext()), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            if (mPlayerPosition != C.TIME_UNSET) {
                mExoPlayer.seekTo(mPlayerPosition);
            }
            mExoPlayer.setPlayWhenReady(autoPlay);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 || mExoPlayer == null) {
            initializePlayer(videoUrl_Parse);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            assert mExoPlayer != null;
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 || mExoPlayer != null) {
            mExoPlayer.getCurrentPosition();
        }
        releasePlayer();
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            autoPlay = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            stepLongDescription.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            previousButton.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            stepLongDescription.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            previousButton.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height = 600;
            mPlayerView.setLayoutParams(params);
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEPS_LIST_INDEX, stepsArrayList);
        outState.putLong(KEY_POSITION, mPlayerPosition);
        outState.putString(STEP_INSTRUCTIONS, stepLongDescriptionUrl);
        outState.putBoolean(AUTO_PLAY_KEY, autoPlay);
        super.onSaveInstanceState(outState);
    }
}



