package com.example.bakingapp.rizwan.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.pojo.Recipes;
import com.example.bakingapp.rizwan.pojo.Steps;
import com.example.bakingapp.rizwan.recyclerviewadapters.StepsAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class StepsListFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickListener {

    private final String TAG = StepsListFragment.class.getSimpleName();

    @BindView(R.id.recycleView_steps)
    RecyclerView mRecyclerView;
    Recipes recipes;
    public static final String STEPS_LIST_INDEX = "list_index";
    ArrayList<Steps> stepsArrayList;
    OnStepClickListener mCallback;

    public interface OnStepClickListener {
        void onClick(Steps stepClick, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepSelectedListener");
        }
    }

    public StepsListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, rootView);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            recipes = getArguments().getParcelable("Recipes");
            stepsArrayList = new ArrayList<>();
            stepsArrayList = recipes.getRecipeSteps();
        }
        if (savedInstanceState != null) {
            stepsArrayList = savedInstanceState.getParcelableArrayList(STEPS_LIST_INDEX);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        Timber.i(TAG,"listSteps: %s", stepsArrayList.size());
        StepsAdapter stepsAdapter = new StepsAdapter(this, stepsArrayList);
        mRecyclerView.setAdapter(stepsAdapter);

        return rootView;
    }

    @Override
    public void onClick(Steps stepClick, int position) {
        mCallback.onClick(stepClick, position);
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEPS_LIST_INDEX, stepsArrayList);
        super.onSaveInstanceState(outState);
    }
}