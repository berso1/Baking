package com.example.android.baking;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.baking.UI.RecipeListActivity;
import com.example.android.baking.UI.RecipeStepFragment;
import com.example.android.baking.data.BakingUtils;
import com.example.android.baking.data.Recipe;

import java.util.List;


public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String RECIPE_ID = "recipe_id";
    private Context mContext;
    private List<Recipe> recipes;

    ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
        recipes = BakingUtils.fetchRecipeData(mContext);

    }

    @Override
    public void onCreate() {
    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        recipes = BakingUtils.fetchRecipeData(mContext);
    }

    @Override
    public void onDestroy() {
        recipes = null;
    }

    @Override
    public int getCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    // This method acts like the onBindViewHolder method in an Adapter
    @Override
    public RemoteViews getViewAt(int position) {
        if (recipes == null || recipes.size() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_view_item);
        Recipe recipe = recipes.get(position);

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
        views.setOnClickFillInIntent(R.id.widget_recipe, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() { return 1; }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}

