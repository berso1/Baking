package com.example.android.baking.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by berso on 8/21/17.
 */

public class Ingredient implements Parcelable{
    private String mIngredient;
    private String mMeasure;
    private int mQuantity;

    public Ingredient(String ingredient,
                      String measure,
                      int quantity){
        mIngredient = ingredient;
        mMeasure    = measure;
        mQuantity   = quantity;
    }

    protected Ingredient(Parcel in) {
        mIngredient = in.readString();
        mMeasure = in.readString();
        mQuantity = in.readInt();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getmIngredient() {
        return mIngredient;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mIngredient);
        parcel.writeString(mMeasure);
        parcel.writeInt(mQuantity);
    }
}
