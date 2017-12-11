package eu.rodrigocamara.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

import eu.rodrigocamara.bakingapp.C;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.activities.MainActivity;
import eu.rodrigocamara.bakingapp.activities.StepListActivity;
import eu.rodrigocamara.bakingapp.network.Controller;
import eu.rodrigocamara.bakingapp.pojos.Recipe$$Parcelable;
import eu.rodrigocamara.bakingapp.utils.Utils;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetConfigureActivity IngredientsWidgetConfigureActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {
    private static String[] ingredients;
    private static int step = 0;

    private static RemoteViews views;
    private static int mMppWidgetId;
    private static AppWidgetManager mAppWidgetManager;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        mMppWidgetId = appWidgetId;
        mAppWidgetManager = appWidgetManager;

        String ingredientList = IngredientsWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        if (ingredientList.equals(context.getResources().getString(R.string.appwidget_text))) {
            views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
            views.setTextViewText(R.id.tv_ingredient, ingredientList);
            views.setTextViewText(R.id.tv_ingredient_details, ingredientList);
        } else {
            ingredients = ingredientList.split(";");

            // Construct the RemoteViews object
            views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

            String aux = ingredients[step];

            views.setTextViewText(R.id.tv_ingredient, Utils.capitalizeString(aux.substring(0, aux.indexOf("#"))));
            views.setTextViewText(R.id.tv_ingredient_details, Utils.capitalizeString(aux.substring(1, aux.indexOf("#"))));
            // Instruct the widget manager to update the widget
            views.setOnClickPendingIntent(R.id.iv_widget_next, getPendingSelfIntent(context, C.WIDGET_CLICK_NEXT));
            views.setOnClickPendingIntent(R.id.iv_widget_previous, getPendingSelfIntent(context, C.WIDGET_CLICK_PREVIOUS));
            views.setOnClickPendingIntent(R.id.btn_widget_app, getPendingSelfIntent(context, C.WIDGET_CLICK_APP));
            views.setOnClickPendingIntent(R.id.btn_widget_ingredients, getPendingSelfIntent(context, C.WIDGET_CLICK_INGREDIENTS));
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    public PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (C.WIDGET_CLICK_NEXT.equals(intent.getAction())) {
            if (step == ingredients.length - 1) {
                Toast.makeText(context, R.string.load_error, Toast.LENGTH_LONG).show();
            } else {
                step = step + 1;
                updateAppWidget(context, mAppWidgetManager, mMppWidgetId);
            }
        } else if (C.WIDGET_CLICK_PREVIOUS.equals(intent.getAction())) {
            if (step == 0) {
                Toast.makeText(context, R.string.load_error, Toast.LENGTH_LONG).show();
            } else {
                step = step - 1;
                updateAppWidget(context, mAppWidgetManager, mMppWidgetId);
            }
        } else if (C.WIDGET_CLICK_APP.equals(intent.getAction())) {
            Intent intent1 = new Intent(context, MainActivity.class);
            context.startActivity(intent1);
        } else if (C.WIDGET_CLICK_INGREDIENTS.equals(intent.getAction())) {
            Intent intent1 = new Intent(context, MainActivity.class);
            context.startActivity(intent1);

        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
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

