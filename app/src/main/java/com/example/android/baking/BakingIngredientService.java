package com.example.android.baking;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.baking.UI.RecipeDetailActivity;

// Created by berso on 9/4/17.


public class BakingIngredientService extends IntentService {

    private static final String SHOW_INGREDIENTS = "com.example.android.baking.action.show_ingredients";

    public BakingIngredientService() {
        super("BakingIngredientService");
    }

    public static void startActionShowIngredients(Context context) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.setAction(SHOW_INGREDIENTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //nothing to do here
    }
}

