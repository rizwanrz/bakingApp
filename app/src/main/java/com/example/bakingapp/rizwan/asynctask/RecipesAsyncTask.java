package com.example.bakingapp.rizwan.asynctask;

import android.os.AsyncTask;
import com.example.bakingapp.rizwan.pojo.Recipes;
import com.example.bakingapp.rizwan.utils.JSONUtils;
import com.example.bakingapp.rizwan.utils.NetworkUtils;
import java.net.URL;
import java.util.ArrayList;

public class RecipesAsyncTask extends AsyncTask<URL, Void, ArrayList<Recipes>> {
    private static final String TAG = RecipesAsyncTask.class.getSimpleName();
    private AsyncTaskInterface listener;

    public RecipesAsyncTask(AsyncTaskInterface listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Recipes> doInBackground(URL... params) {
        URL recipeRequestUrl = NetworkUtils.buildUrl();
        try {
            String jsonRecipeResponse = NetworkUtils
                    .makeHttpRequest(recipeRequestUrl);
            return JSONUtils.extractFeatureFromJson(jsonRecipeResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Recipes> mRecipesList) {
        super.onPostExecute(mRecipesList);
        listener.returnData(mRecipesList);
    }
}