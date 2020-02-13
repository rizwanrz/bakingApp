package com.example.bakingapp.rizwan.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.pojo.Ingredients;
import com.example.bakingapp.rizwan.pojo.Recipes;
import com.example.bakingapp.rizwan.recyclerviewadapters.IngredientsAdapter;
import com.example.bakingapp.rizwan.uiEnhance.VerticalSpacingDecoration;
import com.example.bakingapp.rizwan.widget.RecipeWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class IngredientsFragment extends Fragment {
    private static final String TAG = IngredientsFragment.class.getSimpleName();

    @BindView(R.id.recycleView_ingredients)
    RecyclerView mIngredientRecyclerView;
    Recipes recipes;
    private static final String KEY_INGREDIENTS_LIST = "ingredients_list";
    ArrayList<Ingredients> ingredientsArrayList;

    public IngredientsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipes = getArguments().getParcelable("Recipes");
            ingredientsArrayList = new ArrayList<>();
            ingredientsArrayList = recipes.getRecipeIngredients();

            if (savedInstanceState != null) {
                ingredientsArrayList = savedInstanceState.getParcelableArrayList(KEY_INGREDIENTS_LIST);
            }

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mIngredientRecyclerView.setLayoutManager(mLayoutManager);
            Timber.i(TAG,"listOfIngredients: %s", ingredientsArrayList.size());
            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientsArrayList, getContext());
            mIngredientRecyclerView.setAdapter(ingredientsAdapter);
            mIngredientRecyclerView.addItemDecoration(new VerticalSpacingDecoration(24));

            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences((getActivity()).getApplicationContext());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(ingredientsArrayList);
            prefsEditor.putString("IngredientsList_Widget", json);
            String jsonRecipes = gson.toJson(recipes);
            prefsEditor.putString("Recipes", jsonRecipes);
            prefsEditor.apply();
            Context context = getActivity().getApplicationContext();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, RecipeWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        outState.putParcelableArrayList(KEY_INGREDIENTS_LIST, ingredientsArrayList);
        super.onSaveInstanceState(outState);
    }
}
