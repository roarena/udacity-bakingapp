package eu.rodrigocamara.bakingapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.bakingapp.C;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.adapter.IngredientsAdapter;
import eu.rodrigocamara.bakingapp.adapter.StepsAdapter;
import eu.rodrigocamara.bakingapp.pojos.Recipe;

public class StepListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private Recipe recipe;
    private IngredientsAdapter ingredientsAdapter;
    private StepListLayout stepListLayout;

    @BindView(R.id.stepslist)
    View stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        recipe = Parcels.unwrap(getIntent().getParcelableExtra(C.PARCEL_NAME));
        stepListLayout = new StepListLayout();

        ButterKnife.bind(this);
        ButterKnife.bind(stepListLayout, stepList);

        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
        }

        setupStepsRecyclerView();
        setupIngredientsRecyclerView();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void toggleArrow() {
        if (stepListLayout.ivArrow.getTag().equals(C.ARROW_TAG_DOWN)) {
            stepListLayout.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_48dp);
            stepListLayout.ivArrow.setTag(C.ARROW_TAG_UP);
        } else {
            stepListLayout.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_48dp);
            stepListLayout.ivArrow.setTag(C.ARROW_TAG_DOWN);
        }
    }

    private void setupStepsRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.step_list);
        recyclerView.setAdapter(new StepsAdapter(this, recipe, mTwoPane));
    }

    private void setupIngredientsRecyclerView() {
        ingredientsAdapter = new IngredientsAdapter(recipe.getIngredients());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        stepListLayout.rvIngredients.setLayoutManager(mLayoutManager);
        stepListLayout.rvIngredients.setItemAnimator(new DefaultItemAnimator());
        stepListLayout.rvIngredients.setAdapter(ingredientsAdapter);
        stepListLayout.ivArrow.setTag(C.ARROW_TAG_DOWN);
        stepListLayout.rlIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepListLayout.expandableLayout.toggle();
                toggleArrow();
            }
        });
    }
}
