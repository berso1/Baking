package com.example.android.baking.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

//Created by berso on 8/17/17.


public class Recipe implements Parcelable {

        private int mId;
        private String mName;
        private List<Ingredient> mIngredients;
        private List<Step> mSteps;
        private int mServings;
        private String mImage;

        public Recipe(int id,
                      String name,
                      List<Ingredient> ingredients,
                      List<Step> steps,
                      int servings,
                      String image){

            mId = id;
            mName = name;
            mIngredients = ingredients;
            mSteps = steps;
            mServings = servings;
            mImage = image;
        }

    private Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mIngredients = in.createTypedArrayList(Ingredient.CREATOR);
        mSteps = in.createTypedArrayList(Step.CREATOR);
        mServings = in.readInt();
        mImage = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getmId() {
            return mId;
        }

        public String getmName() {
            return mName;
        }

        public List<Ingredient> getmIngredients() {
            return mIngredients;
        }

        public List<Step> getmSteps() {
            return mSteps;
        }

        public int getmServings() {
            return mServings;
        }

        public String getmImage() {
            return mImage;
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeTypedList(mIngredients);
        parcel.writeTypedList(mSteps);
        parcel.writeInt(mServings);
        parcel.writeString(mImage);
    }
}

