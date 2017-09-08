package com.example.android.baking;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.baking.UI.RecipeDetailActivity;

// Implementation of App Widget functionality.

public class BakingWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
        RemoteViews rv;
        rv = getBakingGridRemoteView(context);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getBakingGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.listViewWidget,intent);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listViewWidget, appPendingIntent);
        // Handle empty list
        views.setEmptyView(R.id.listViewWidget, R.id.empty_view);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        BakingIngredientService.startActionShowIngredients(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
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

