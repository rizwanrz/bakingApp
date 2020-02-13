package com.example.bakingapp.rizwan.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.asynctask.AsyncTaskInterface;
import com.example.bakingapp.rizwan.asynctask.RecipesAsyncTask;
import com.example.bakingapp.rizwan.pojo.Recipes;
import com.example.bakingapp.rizwan.recyclerviewadapters.RecipesAdapter;
import com.example.bakingapp.rizwan.uiEnhance.VerticalSpacingDecoration;
import com.example.bakingapp.rizwan.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler, AsyncTaskInterface {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recycleView_main)
    RecyclerView mRecipeRecyclerView;
    private RecipesAdapter recipesAdapter;
    private ArrayList<Recipes> recipesArrayList = new ArrayList<>();
    private Context context;
    private static final String KEY_RECIPES_LIST = "recipes_list";
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        ButterKnife.bind(this);
        mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        recipesAdapter = new RecipesAdapter(this, recipesArrayList, context);
        mRecipeRecyclerView.setAdapter(recipesAdapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        mRecipeRecyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceState == null) {
            if (isNetworkStatusAvailable(this)) {
                RecipesAsyncTask myTask = new RecipesAsyncTask(this);
                myTask.execute(NetworkUtils.buildUrl());
            } else {
                Timber.d(TAG, "No Internet Connection");
                Snackbar.make(mCoordinatorLayout, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new MyClickListener())
                        .show();
            }
        } else {
            recipesArrayList = savedInstanceState.getParcelableArrayList(KEY_RECIPES_LIST);
            recipesAdapter.setRecipesList(recipesArrayList);
        }
        mRecipeRecyclerView.addItemDecoration(new VerticalSpacingDecoration(10));
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            RecipesAsyncTask myTask = new RecipesAsyncTask(MainActivity.this);
            myTask.execute();
        }
    }

    @Override
    public void returnData(ArrayList<Recipes> simpleJsonRecipeData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (null != simpleJsonRecipeData) {
            recipesAdapter = new RecipesAdapter(this, simpleJsonRecipeData, MainActivity.this);
            recipesArrayList = simpleJsonRecipeData;
            mRecipeRecyclerView.setAdapter(recipesAdapter);
            recipesAdapter.setRecipesList(recipesArrayList);

        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onClick(Recipes recipes) {
        Intent intent = new Intent(MainActivity.this, IngredientStepsActivity.class);
        intent.putExtra("Recipes", recipes);
        startActivity(intent);
    }

    // Display if there is no internet connection
    public void showErrorMessage() {
        Snackbar.make(mCoordinatorLayout, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new MyClickListener())
                .show();
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public static boolean isNetworkStatusAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_RECIPES_LIST, recipesArrayList);
        super.onSaveInstanceState(outState);
    }
}
