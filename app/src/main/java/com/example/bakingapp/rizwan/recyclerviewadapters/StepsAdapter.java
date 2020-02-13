package com.example.bakingapp.rizwan.recyclerviewadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.pojo.Steps;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {
    private static final String TAG = StepsAdapter.class.getName();
    private ArrayList<Steps> stepsList = new ArrayList<Steps>();
    private StepsAdapter.StepsAdapterOnClickListener mClickListener;

    public interface StepsAdapterOnClickListener {
        void onClick(Steps stepClick, int position);
    }

    public StepsAdapter(StepsAdapterOnClickListener clickListener, ArrayList<Steps> stepsList) {
        mClickListener = clickListener;
        this.stepsList = stepsList;
    }

    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_short_desc)
        public TextView stepShortDescription;

        public StepsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Timber.d(TAG, adapterPosition);
            Steps position = stepsList.get(adapterPosition);
            mClickListener.onClick(position, adapterPosition);
        }
    }

    @Override
    public StepsAdapter.StepsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.steps_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new StepsAdapter.StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepsAdapterViewHolder holder, int position) {
        final Steps stepsView = stepsList.get(position);
        holder.stepShortDescription.setText(stepsView.getStepShortDescription());

    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public void setStepsList(ArrayList<Steps> mStepsList) {
        this.stepsList = mStepsList;
        notifyDataSetChanged();
    }
}



