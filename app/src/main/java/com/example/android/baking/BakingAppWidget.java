package com.example.android.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.android.baking.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i=0;i<appWidgetIds.length;i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
            SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.app",
                    Context.MODE_PRIVATE);
            String recipeName = sharedPreferences.getString("currentRecipe","");

            views.setTextViewText(R.id.recipe_name, recipeName);
            Intent intent = new Intent(context, ListViewWidgetService.class);
            views.setRemoteAdapter(R.id.listViewWidget,intent);

            Intent appIntent = new Intent(context, MainActivity.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.listViewWidget, appPendingIntent);
            // Handle empty list
            views.setEmptyView(R.id.listViewWidget, R.id.empty_view);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

