package com.example.bakingapp.rizwan.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable
{
    private String ingredientQuantity;
    private String ingredientMeasure;
    private String ingredientName;


    public Ingredients(String ingredientQuantity, String ingredientMeasure, String ingredientName)
    {
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientMeasure = ingredientMeasure;
        this.ingredientName = ingredientName;
    }

    public void setIngredientQuantity(String ingredientQuantity)
    {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getIngredientQuantity()
    {
        return ingredientQuantity;
    }

    public void setIngredientMeasure(String ingredientMeasure)
    {
        this.ingredientMeasure = ingredientMeasure;
    }

    public String getIngredientMeasure()
    {
        return ingredientMeasure;
    }

    public void setName(String ingredientName)
    {
        this.ingredientName = ingredientName;
    }

    public String getIngredientName()
    {
        return ingredientName;
    }

    protected Ingredients(Parcel in)
    {
        ingredientQuantity = in.readString();
        ingredientMeasure = in.readString();
        ingredientName = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(ingredientQuantity);
        dest.writeString(ingredientMeasure);
        dest.writeString(ingredientName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredients> CREATOR = new Parcelable.Creator<Ingredients>()
    {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
}




