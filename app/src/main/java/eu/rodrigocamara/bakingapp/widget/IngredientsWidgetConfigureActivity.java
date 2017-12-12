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
import eu.rodrigocamara.bakingapp.C;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.network.Controller;
import eu.rodrigocamara.bakingapp.network.interfaces.UIController;
import eu.rodrigocamara.bakingapp.pojos.IngredientsItem;
import eu.rodrigocamara.bakingapp.pojos.Recipe;

public class IngredientsWidgetConfigureActivity extends Activity implements UIController {

    @BindView(R.id.widget_cfg_layout)
    RelativeLayout rlCfg;

    @BindView(R.id.pb_widget)
    ProgressBar pbLoading;

    @BindView(R.id.btn_save_widget)
    Button btnSave;

    @BindView(R.id.spinner)
    Spinner spnRecipes;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
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

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ingredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public IngredientsWidgetConfigureActivity() {
        super();
    }

    static void saveIngredientsPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(C.PREFS_NAME, 0).edit();
        prefs.putString(C.PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    static String loadIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(C.PREFS_NAME, 0);
        String titleValue = prefs.getString(C.PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(C.PREFS_NAME, 0).edit();
        prefs.remove(C.PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredients_widget_configure);
        controller = new Controller();
        controller.start(this);
        ButterKnife.bind(this);

        btnSave.setOnClickListener(mOnClickListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

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

