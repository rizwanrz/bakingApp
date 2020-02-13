package com.example.bakingapp.rizwan.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Recipes implements Parcelable {
    private String recipeId;
    private String recipeName;
    private String recipeImage;
    private String recipeServings;
    private ArrayList<Ingredients> recipeIngredients;
    private ArrayList<Steps> recipeSteps;

    public Recipes(String recipeId, String recipeName, String recipeImage, String recipeServings, ArrayList<Ingredients> recipeIngredients, ArrayList<Steps> recipeSteps) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.recipeServings = recipeServings;
        this.recipeIngredients = recipeIngredients;
        this.recipeSteps = recipeSteps;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getNumberOfServings() {
        return recipeServings;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public ArrayList<Ingredients> getRecipeIngredients() {
        return recipeIngredients;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public ArrayList<Steps> getRecipeSteps() {
        return recipeSteps;
    }

    protected Recipes(Parcel in) {
        recipeId = in.readString();
        recipeName = in.readString();
        recipeImage = in.readString();
        recipeServings = in.readString();

        if (in.readByte() == 0x01) {
            recipeIngredients = new ArrayList<Ingredients>();
            in.readList(recipeIngredients, Ingredients.class.getClassLoader());
        } else {
            recipeIngredients = null;
        }
        if (in.readByte() == 0x01) {
            recipeSteps = new ArrayList<Steps>();
            in.readList(recipeSteps, Steps.class.getClassLoader());
        } else {
            recipeSteps = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeId);
        dest.writeString(recipeName);
        dest.writeString(recipeImage);
        dest.writeString(recipeServings);
        if (recipeIngredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recipeIngredients);
        }
        if (recipeSteps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recipeSteps);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipes> CREATOR = new Parcelable.Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };
}


