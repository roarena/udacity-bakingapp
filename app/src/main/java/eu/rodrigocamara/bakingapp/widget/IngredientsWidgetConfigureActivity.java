package eu.rodrigocamara.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.network.Controller;
import eu.rodrigocamara.bakingapp.network.interfaces.UIController;
import eu.rodrigocamara.bakingapp.pojos.IngredientsItem;
import eu.rodrigocamara.bakingapp.pojos.Recipe;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends Activity implements UIController {

    private static final String PREFS_NAME = "eu.rodrigocamara.bakingapp.widget.IngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @BindView(R.id.widget_cfg_layout)
    RelativeLayout rlCfg;

    @BindView(R.id.pb_widget)
    ProgressBar pbLoading;

    @BindView(R.id.btn_save_widget)
    Button btnSave;

    @BindView(R.id.spinner)
    Spinner spnRecipes;

    private Controller controller;
    private List<Recipe> recipesList;
    private IngredientsWidget ingredientsWidget;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = IngredientsWidgetConfigureActivity.this;

            StringBuilder stringBuilder = new StringBuilder();
            for (IngredientsItem ingredientsItem : recipesList.get(spnRecipes.getSelectedItemPosition()).getIngredients()) {
                stringBuilder.append(ingredientsItem.getIngredient() + "#");
                stringBuilder.append(ingredientsItem.getQuantity() + ingredientsItem.getMeasure());
                stringBuilder.append(";");
            }
            saveIngredientsPref(context, mAppWidgetId, stringBuilder.toString());

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ingredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public IngredientsWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveIngredientsPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredients_widget_configure);
        controller = new Controller();
        controller.start(this);
        ButterKnife.bind(this);

        btnSave.setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        ingredientsWidget = new IngredientsWidget();
    }

    @Override
    public void onResponseOK(List<Recipe> recipesList) {
        rlCfg.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
        this.recipesList = recipesList;
        setRecipeNames();
    }

    @Override
    public void onResponseFail() {
        Toast.makeText(this, R.string.load_error, Toast.LENGTH_LONG).show();
        pbLoading.setVisibility(View.GONE);
    }

    private void setRecipeNames() {
        ArrayList<String> recipeNames = new ArrayList<>();
        for (Recipe recipe : recipesList) {
            recipeNames.add(recipe.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, recipeNames);
        spnRecipes.setAdapter(adapter);
    }
}

