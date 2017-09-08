package com.example.android.baking;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

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

        /**
         * Starts this service to perform WaterPlant action with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        public static void startActionShowIngredients(Context context, long plantId) {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.setAction(SHOW_INGREDIENTS);
          //  intent.putExtra(EXTRA_PLANT_ID, plantId);
            context.startService(intent);
        }

        /**
         * Starts this service to perform UpdatePlantWidgets action with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */

        public static void startActionUpdatePlantWidgets(Context context) {
            Intent intent = new Intent(context, BakingIngredientService.class);
            intent.setAction(ACTION_UPDATE_PLANT_WIDGETS);
            context.startService(intent);
        }


        @Override
        protected void onHandleIntent(Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if (SHOW_INGREDIENTS.equals(action)) {
                    handleActionUpdatePlantWidgets();
                } else if (ACTION_UPDATE_PLANT_WIDGETS.equals(action)) {
                    handleActionUpdatePlantWidgets();
                }
            }
        }

/*
        private void handleActionWaterPlant(long plantId) {
            Uri SINGLE_PLANT_URI = ContentUris.withAppendedId(
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build(), plantId);
            ContentValues contentValues = new ContentValues();
            long timeNow = System.currentTimeMillis();
            contentValues.put(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);
            // Update only if that plant is still alive
            getContentResolver().update(
                    SINGLE_PLANT_URI,
                    contentValues,
                    PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME + ">?",
                    new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)});
            // Always update widgets after watering plants
            startActionUpdatePlantWidgets(this);
        }
*/
        /**
         * Handle action UpdatePlantWidgets in the provided background thread
         */
        private void handleActionUpdatePlantWidgets() {



            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
            //Trigger data update to handle the GridView widgets and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);
        }


    //Query to get the plant that's most in need for water (last watered)
            /*
            Uri PLANT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build();
            Cursor cursor = getContentResolver().query(
                    PLANT_URI,
                    null,
                    null,
                    null,
                    PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME
            );
            // Extract the plant details
            int imgRes = R.drawable.grass; // Default image in case our garden is empty
            boolean canWater = false; // Default to hide the water drop button
            long plantId = INVALID_PLANT_ID;
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int idIndex = cursor.getColumnIndex(PlantContract.PlantEntry._ID);
                int createTimeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_CREATION_TIME);
                int waterTimeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME);
                int plantTypeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_PLANT_TYPE);
                plantId = cursor.getLong(idIndex);
                long timeNow = System.currentTimeMillis();
                long wateredAt = cursor.getLong(waterTimeIndex);
                long createdAt = cursor.getLong(createTimeIndex);
                int plantType = cursor.getInt(plantTypeIndex);
                cursor.close();
                canWater = (timeNow - wateredAt) > PlantUtils.MIN_AGE_BETWEEN_WATER &&
                        (timeNow - wateredAt) < PlantUtils.MAX_AGE_WITHOUT_WATER;
                imgRes = PlantUtils.getPlantImageRes(this, timeNow - createdAt, timeNow - wateredAt, plantType);
            }
            */
}

