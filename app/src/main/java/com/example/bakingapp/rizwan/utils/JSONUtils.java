package com.example.bakingapp.rizwan.utils;

import android.text.TextUtils;

import com.example.bakingapp.rizwan.pojo.Ingredients;
import com.example.bakingapp.rizwan.pojo.Recipes;
import com.example.bakingapp.rizwan.pojo.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;

public class JSONUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String KEY_RECIPE_ID = "id";
    private static final String KEY_RECIPE_NAME = "name";
    private static final String KEY_RECIPE_IMAGE = "image";
    private static final String KEY_SERVINGS_NUMBER = "servings";
    private static final String KEY_INGREDIENT_QUANTITY = "quantity";
    private static final String KEY_STEPS_SHORT_DESC = "shortDescription";
    private static final String KEY_STEPS_LONG_DESC = "description";
    private static final String KEY_STEPS_VIDEO_URL = "videoURL";
    private static final String KEY_STEPS_THUMBNAIL_URL = "thumbnailURL";
    private static final String KEY_INGREDIENT_MEASURE = "measure";
    private static final String KEY_INGREDIENT_NAME = "ingredient";
    private static final String KEY_STEPS_ID = "id";

    public JSONUtils() {
    }

    public static ArrayList<Recipes> extractFeatureFromJson(String recipeJSON) {
        if (TextUtils.isEmpty(recipeJSON)) {
            return null;
        }
        ArrayList<Recipes> recipes = new ArrayList<>();
        try {
            JSONArray recipeArray = new JSONArray(recipeJSON);

            for (int i = 0; i < recipeArray.length(); i++) {
                // Get a single recipe description at position i within the list of recipes
                JSONObject currentRecipe = recipeArray.getJSONObject(i);

                String recipeId = currentRecipe.optString(KEY_RECIPE_ID);
                String recipeName = currentRecipe.optString(KEY_RECIPE_NAME);
                String recipeImage = currentRecipe.optString(KEY_RECIPE_IMAGE);
                String recipeServings = currentRecipe.optString(KEY_SERVINGS_NUMBER);

                Timber.d(TAG, recipeName);
                ArrayList<Ingredients> ingredients = new ArrayList<>();
                JSONArray ingredientsArray = currentRecipe.getJSONArray("ingredients");

                for (int j = 0; j < ingredientsArray.length(); j++) {
                    JSONObject currentIngredient = ingredientsArray.getJSONObject(j);
                    String ingredientQuantity = currentIngredient.optString(KEY_INGREDIENT_QUANTITY);
                    String ingredientMeasure = currentIngredient.optString(KEY_INGREDIENT_MEASURE);
                    String ingredientName = currentIngredient.optString(KEY_INGREDIENT_NAME);
                    Ingredients ingredient = new Ingredients(ingredientQuantity, ingredientMeasure, ingredientName);
                    ingredients.add(ingredient);
                }

                ArrayList<Steps> steps = new ArrayList<>();
                JSONArray stepsArray = currentRecipe.getJSONArray("steps");

                for (int k = 0; k < stepsArray.length(); k++) {
                    JSONObject currentStep = stepsArray.getJSONObject(k);
                    int stepId = currentStep.optInt(KEY_STEPS_ID);
                    String stepShortDescription = currentStep.optString(KEY_STEPS_SHORT_DESC);
                    String stepLongDescription = currentStep.optString(KEY_STEPS_LONG_DESC);
                    String videoURL = currentStep.optString(KEY_STEPS_VIDEO_URL);
                    String thumbnailURL = currentStep.optString(KEY_STEPS_THUMBNAIL_URL);

                    Steps step = new Steps(stepId, stepShortDescription, stepLongDescription, videoURL, thumbnailURL);
                    steps.add(step);
                }

                Recipes recipe = new Recipes(recipeId, recipeName, recipeImage, recipeServings, ingredients, steps);
                recipes.add(recipe);
            }

        } catch (JSONException e) {
            Timber.d(e);
        }
        return recipes;
    }
}
