package com.example.bakingapp.rizwan.recyclerviewadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.pojo.Recipes;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {
    private static final String TAG = RecipesAdapter.class.getSimpleName();

    private ArrayList<Recipes> recipesList = new ArrayList<Recipes>();
    private Context context;
    public static final int IMAGE_HEIGHT = 185;
    public static final int IMAGE_WIDTH = 50;
    private RecipesAdapterOnClickHandler mRecipeClickHandler;

    public interface RecipesAdapterOnClickHandler {
        void onClick(Recipes imageRecipeClick);
    }

    public RecipesAdapter(RecipesAdapterOnClickHandler recipeClickHandler, ArrayList<Recipes> recipesList, Context context) {
        mRecipeClickHandler = recipeClickHandler;
        this.recipesList = recipesList;
        this.context = context;
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.recipeView)
        TextView recipeView;

        @BindView(R.id.servingsView)
        TextView servingsView;

        public RecipesAdapterViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipes imageRecipeClick = recipesList.get(adapterPosition);
            mRecipeClickHandler.onClick(imageRecipeClick);
        }
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RecipesAdapterViewHolder holder, int position) {
        final Recipes recipesView = recipesList.get(position);
        holder.recipeView.setText(recipesView.getRecipeName());
        String servings = context.getString(R.string.recipe_serves_how_many,
                recipesView.getNumberOfServings());
        holder.servingsView.setText(servings);

        if (!recipesView.getRecipeImage().isEmpty()) {
            Picasso.with(context)
                    .load(recipesView.getRecipeImage())
                    .error(R.drawable.user_placeholder_error)
                    .into(holder.imageView);

        } else if (recipesView.getRecipeName().equals("Nutella Pie")) {
            Picasso.with(context)
                    .load(R.drawable.nutella_pie)
                    .error(R.drawable.user_placeholder_error)
                    .into(holder.imageView);
        } else if (recipesView.getRecipeName().equals("Brownies")) {
            Picasso.with(context)
                    .load(R.drawable.brownies_recipe)
                    .into(holder.imageView);

        } else if (recipesView.getRecipeName().equals("Yellow Cake")) {
            Picasso.with(context)
                    .load(R.drawable.yellow_cake)
                    .error(R.drawable.user_placeholder_error)
                    .into(holder.imageView);

        } else if (recipesView.getRecipeName().equals("Cheesecake")) {
            Picasso.with(context)
                    .load(R.drawable.cheesecake_recipe)
                    .error(R.drawable.user_placeholder_error)
                    .into(holder.imageView);
        } else {
            Picasso.with(context)
                    .load(R.drawable.user_placeholder_error)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public void setRecipesList(ArrayList<Recipes> mRecipesList) {
        this.recipesList = mRecipesList;
        notifyDataSetChanged();
    }
}
