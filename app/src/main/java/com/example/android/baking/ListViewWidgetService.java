package com.example.android.baking;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.baking.data.BakingUtils;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.ui.RecipeFragment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berso on 9/12/17.
 */

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory  implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<Recipe> mRecipes;
    Recipe mCurrentRecipe;
    List<String> mIngredients;

    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        new WidgetLoadTask().execute();
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mIngredients == null || mIngredients.size() == 0 )return 0;
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mIngredients == null || mIngredients.size() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_view_item);
        views.setTextViewText(R.id.widget_recipe, mIngredients.get(position));

        Bundle extras = new Bundle();
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.root_view, fillInIntent);


        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;  }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    public class WidgetLoadTask extends AsyncTask<URL, Integer, List<Ingredient>> {
        List<Ingredient> ingredients;
        @Override
        protected List<Ingredient> doInBackground(URL... urls) {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("com.example.app",
                    Context.MODE_PRIVATE);
            String recipeName = sharedPreferences.getString("currentRecipe","");
            int id = sharedPreferences.getInt("recipe_id",0);
            int position = id - 1;
            ingredients = BakingUtils.fetchIngredientData(RecipeFragment.BAKING_DATA_URL,position);
            return ingredients;
        }

        @Override
        protected void onPostExecute(List<Ingredient> ingredients) {
            super.onPostExecute(ingredients);
            mIngredients = getIngredients(ingredients);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, BakingAppWidget.class));
            //Trigger data update to handle the ListView widgets and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);
        }


        private List<String> getIngredients(List<Ingredient> recipeIngredients){
            List<String> ingredientList = new ArrayList<>();
            if(recipeIngredients == null) return null;
            for (int j = 0; j < recipeIngredients.size();j++){
                Ingredient currentIngredient = recipeIngredients.get(j);
                String formedIngredient = currentIngredient.getmIngredient() +": "+
                        currentIngredient.getmQuantity()+" "+
                        currentIngredient.getmMeasure();
                ingredientList.add(formedIngredient);
            }
            return ingredientList;
        }
    }
}