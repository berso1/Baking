package com.example.android.baking;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.baking.data.BakingUtils;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.ui.RecipeFragment;
import com.example.android.baking.ui.RecipeListActivity;
import com.example.android.baking.ui.RecipeStepFragment;

import java.net.URL;
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

    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        new WidgetLoadTask().execute();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mRecipes == null || mRecipes.size() == 0 )return 0;
        return mRecipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mRecipes == null || mRecipes.size() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_view_item);
        Recipe recipe = mRecipes.get(position);

        String name = recipe.getmName();
        // Update the recipe name
        views.setImageViewResource(R.id.widget_image,R.drawable.ic_cake);
        views.setTextViewText(R.id.widget_recipe, name);

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putParcelable("currentRecipe", recipe);
        extras.putParcelable("currentStep",recipe.getmSteps().get(0));
        extras.putString(RecipeStepFragment.ARG_ITEM, RecipeListActivity.INGREDIENTS_TITLE);
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
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    public class WidgetLoadTask extends AsyncTask<URL, Integer, List<Recipe>> {
        @Override
        protected List<Recipe> doInBackground(URL... urls) {
            return BakingUtils.fetchRecipeData(RecipeFragment.BAKING_DATA_URL);
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);
            mRecipes = recipes;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, BakingAppWidget.class));
            //Trigger data update to handle the ListView widgets and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);
        }
    }
}