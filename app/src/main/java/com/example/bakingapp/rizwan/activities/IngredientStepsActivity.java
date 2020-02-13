package com.example.bakingapp.rizwan.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.fragments.IngredientsFragment;
import com.example.bakingapp.rizwan.fragments.StepsListFragment;
import com.example.bakingapp.rizwan.fragments.VideoFragment;
import com.example.bakingapp.rizwan.pojo.Recipes;
import com.example.bakingapp.rizwan.pojo.Steps;

import java.util.ArrayList;

import timber.log.Timber;

public class IngredientStepsActivity extends AppCompatActivity implements StepsListFragment.OnStepClickListener {

    private static final String TAG = IngredientStepsActivity.class.getSimpleName();
    private Context context;
    Recipes recipes;
    public int stepIndex;
    public static ArrayList<Steps> stepsArrayList;
    public boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientsteps);

        context = getApplicationContext();
        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getExtras() != null) {
                recipes = getIntent().getExtras().getParcelable("Recipes");
                stepsArrayList = new ArrayList<>();
                stepsArrayList = recipes.getRecipeSteps();
                FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle ingredientsBundle = new Bundle();
                ingredientsBundle.putParcelable("Recipes", recipes);
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                ingredientsFragment.setArguments(ingredientsBundle);
                fragmentManager.beginTransaction().replace(R.id.ingredients_fragment_container, ingredientsFragment).commit();
                Bundle stepsBundle = new Bundle();
                stepsBundle.putParcelable("Recipes", recipes);
                StepsListFragment stepsListFragment = new StepsListFragment();
                stepsListFragment.setArguments(stepsBundle);
                fragmentManager.beginTransaction().replace(R.id.steps_fragment_container, stepsListFragment).commit();
            }
        }

        if (findViewById(R.id.tablet_detail_layout) != null) {
            Timber.d(TAG,"2 Pane mode: Tablet");
            mTwoPane = true;
        } else {
            Timber.d(TAG,"Single Pane mode: Phone");
            mTwoPane = false;
        }

        if (recipes != null)
            Timber.i("Recipe Name on Titile: %s", recipes.getRecipeName());
            setTitle(recipes.getRecipeName());
    }

    @Override
    public void onClick(Steps stepClicked, int stepPosition) {
        stepIndex = stepPosition;
        if (mTwoPane) {
            Timber.d(TAG, "Fragment launch");
            Bundle stepsVideoBundle = new Bundle();
            stepsVideoBundle.putString("recipeName", recipes.getRecipeImage());
            stepsVideoBundle.putParcelable("Steps", stepClicked);
            stepsVideoBundle.putBoolean("TwoPane", mTwoPane);
            stepsVideoBundle.putInt("StepIndex", stepIndex);
            stepsVideoBundle.putParcelableArrayList("StepsArrayList", stepsArrayList);

            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setArguments(stepsVideoBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.video_fragment_container, videoFragment).commit();
        } else {
            Timber.i(stepClicked.getStepShortDescription());
            Intent intent = new Intent(IngredientStepsActivity.this, VideoPhoneActivity.class);
            intent.putExtra("recipeName", recipes.getRecipeName());
            intent.putExtra("Steps", stepClicked);
            intent.putExtra("TwoPane", mTwoPane);
            intent.putExtra("StepIndex", stepIndex);
            intent.putParcelableArrayListExtra("StepsArrayList", stepsArrayList);
            Timber.i("ingredient " + stepsArrayList.size() + "");
            Timber.d(TAG, "Video Activity launch");
            startActivity(intent);
        }
    }
}






