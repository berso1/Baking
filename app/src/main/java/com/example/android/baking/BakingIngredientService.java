package com.example.android.baking;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.baking.UI.RecipeDetailActivity;

/**
 * Created by berso on 9/4/17.
 */

public class BakingIngredientService  extends IntentService {

        public static final String SHOW_INGREDIENTS = "com.example.android.baking.action.show_ingredients";
        public static final String ACTION_UPDATE_PLANT_WIDGETS = "com.example.android.mygarden.action.update_plant_widgets";
        public static final String EXTRA_PLANT_ID = "com.example.android.mygarden.extra.PLANT_ID";;

        public BakingIngredientService() {
            super("BakingIngredientService");
        }

        public static void startActionShowIngredients(Context context, long plantId) {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.setAction(SHOW_INGREDIENTS);
            context.startService(intent);
        }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //nothing to do here
    }
}

