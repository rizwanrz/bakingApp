package com.example.bakingapp.rizwan.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


import com.example.bakingapp.rizwan.R;
import com.example.bakingapp.rizwan.activities.IngredientStepsActivity;
import com.example.bakingapp.rizwan.activities.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_VIEW_DETAILS = "com.example.bakingapp.RecipeWidgetProvider.ACTION_VIEW_DETAILS";

    public static final String EXTRA_ITEM = "com.example.bakingapp.RecipeWidgetProvider.EXTRA_ITEM";

    public void setPendingIntentTemplate(int viewId, PendingIntent pendingIntentTemplate) {
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context.getApplicationContext(),
                    RecipeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setRemoteAdapter(R.id.appwidget_list, intent);
            views.setEmptyView(R.id.appwidget_list, R.id.empty);

            Intent detailIntent = new Intent(context, IngredientStepsActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(new Intent(context, MainActivity.class));
            stackBuilder.addNextIntent(detailIntent);

            PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.appwidget_list, pIntent);

            appWidgetManager.updateAppWidget(widgetId, views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_VIEW_DETAILS.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), RecipeWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list);
        }
    }
}

