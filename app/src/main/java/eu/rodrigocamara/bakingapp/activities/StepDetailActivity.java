package eu.rodrigocamara.bakingapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import eu.rodrigocamara.bakingapp.C;
import eu.rodrigocamara.bakingapp.R;

public class StepDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putInt(C.STEP, getIntent().getIntExtra(C.STEP, 0));
            arguments.putParcelable(C.RECIPE, getIntent().getParcelableExtra(C.RECIPE));

            StepDetailFragment fragment = new StepDetailFragment();

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
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
}
