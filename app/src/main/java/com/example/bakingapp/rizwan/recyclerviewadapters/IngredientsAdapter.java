package com.example.bakingapp.rizwan.recyclerviewadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.pojo.Ingredients;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {
    private static final String TAG = IngredientsAdapter.class.getSimpleName();

    private ArrayList<Ingredients> ingredientsList = new ArrayList<Ingredients>();
    private Context context;

    public IngredientsAdapter(ArrayList<Ingredients> ingredientsList, Context context) {
        this.ingredientsList = ingredientsList;
        this.context = context;
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_quantity)
        public TextView ingredientQuantity;

        @BindView(R.id.ingredient_measure)
        public TextView ingredientMeasure;

        @BindView(R.id.ingredient_name)
        public TextView ingredientName;

        public IngredientsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapterViewHolder holder, int position) {
        final Ingredients ingredientsView = ingredientsList.get(position);
        holder.ingredientQuantity.setText(ingredientsView.getIngredientQuantity());
        holder.ingredientMeasure.setText(ingredientsView.getIngredientMeasure());
        holder.ingredientName.setText(ingredientsView.getIngredientName());
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public void setIngredientsList(ArrayList<Ingredients> mIngredientsList) {
        this.ingredientsList = mIngredientsList;
        notifyDataSetChanged();
    }
}
