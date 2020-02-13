package com.example.bakingapp.rizwan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.fragments.VideoFragment;
import com.example.bakingapp.rizwan.pojo.Steps;

import java.util.ArrayList;

import timber.log.Timber;

public class VideoPhoneActivity extends AppCompatActivity {
    private static final String TAG = VideoPhoneActivity.class.getSimpleName();

    private Steps stepClicked;
    public boolean mTwoPane;
    public int stepIndex;
    public ArrayList<Steps> stepsArrayList;
    public static final String STEPS_LIST_INDEX = "list_index";
    public static final String KEY_VIDEO_FRAGMENT = "videoFragment";
    public VideoFragment videoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videophone);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            videoFragment = new VideoFragment();
            Intent intent = getIntent();
            setTitle(intent.getStringExtra("recipeName"));
            Timber.d(TAG, getTitle());
            if (intent.getExtras() != null) {
                stepClicked = intent.getExtras().getParcelable("Steps");
                stepIndex = intent.getIntExtra("StepIndex", -1);
                mTwoPane = intent.getBooleanExtra("TwoPane", true);
                stepsArrayList = new ArrayList<>();
                stepsArrayList = intent.getParcelableArrayListExtra("StepsArrayList");
                Bundle bundle = new Bundle();
                bundle.putParcelable("Steps", stepClicked);
                bundle.putBoolean("TwoPane", mTwoPane);
                bundle.putInt("StepIndex", stepIndex);
                bundle.putParcelableArrayList("StepsArrayList", stepsArrayList);
                videoFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.video_fragment_container, videoFragment).commit();
            }
        } else {
            stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_LIST_INDEX);
            videoFragment = (VideoFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_VIDEO_FRAGMENT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STEPS_LIST_INDEX, stepsArrayList);
        getSupportFragmentManager().putFragment(outState, KEY_VIDEO_FRAGMENT, videoFragment);
        super.onSaveInstanceState(outState);
    }
}

